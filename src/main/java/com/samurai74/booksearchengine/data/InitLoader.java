package com.samurai74.booksearchengine.data;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.samurai74.booksearchengine.domain.entity.Author;
import com.samurai74.booksearchengine.domain.entity.Book;
import com.samurai74.booksearchengine.repository.AuthorRepo;
import com.samurai74.booksearchengine.repository.BookRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InitLoader implements CommandLineRunner {

    @Value("${CSV_URL}")
    private String csvUrl;

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;

    void ImportCSV() throws IOException, CsvValidationException {
        // download csv
        InputStream csvStream = downloadCSV(csvUrl);

        // parse csv
        List<String[]> csvLines = parseCSV(csvStream);
        // insert into db
        insert(csvLines);

    }
    @Transactional
    void insert(List<String[]> csvLines){


        var authorCache = new HashMap<String, Author>();
        log.info("---Started inserting---");
        long startTime =System.currentTimeMillis();

        List<Book> booksToSave = new ArrayList<>();
        for(int i=1 ; i<csvLines.size() ; i++){
            var title = csvLines.get(i)[1];
            var authorName = csvLines.get(i)[2];
            var rating = csvLines.get(i)[3];
            var description = csvLines.get(i)[4];
            var language = csvLines.get(i)[5];
            var isbn = csvLines.get(i)[6];
            var bookFormat = csvLines.get(i)[7];
            var edition = csvLines.get(i)[8];
            var pages = csvLines.get(i)[9];
            var publisher = csvLines.get(i)[10];
            var publishDate = csvLines.get(i)[11];
            var firstPublishDate = csvLines.get(i)[12];
            var likedPercent = csvLines.get(i)[13];
            var price = csvLines.get(i)[14];

            // bookId,title,author,rating,description,language,isbn,bookFormat,edition,pages,publisher,publishDate,firstPublishDate,likedPercent,price
            Book book = new Book();
            book.setTitle(title);
            book.setAuthors(new HashSet<>());
            if(authorName!=null && !authorName.isEmpty()){
                Author author;
                if(authorCache.containsKey(authorName)){
                   author = authorCache.get(authorName);
                }
                else{
                    author= authorRepo.findByNameIgnoreCase(authorName).orElseGet(()->{
                        var newauthor = new Author();
                        newauthor.setName(authorName);
                        newauthor.setBooks(new HashSet<>());
                        return newauthor;
                    });
                    authorCache.put(authorName, author);
                }

                book.addAuthor(author);
            }
            book.setRating(new BigDecimal(rating));
            book.setDescription(description);
            book.setLanguage(language);
            book.setIsbn(isbn);
            book.setBookFormat(bookFormat);
            book.setEdition(edition);
            book.setPages(Integer.parseInt(pages));
            book.setPublisher(publisher);
            book.setPublishDate(LocalDate.parse(publishDate));
            book.setFirstPublishDate(LocalDate.parse(firstPublishDate));
            book.setLikedPercent(new BigDecimal(likedPercent));
            book.setPrice(new BigDecimal(price));
            booksToSave.add(book);
        }

        log.info("Saving {} books in bulk...", booksToSave.size());
        bookRepo.saveAll(booksToSave);

        long endTime =System.currentTimeMillis();
        log.info("---Ended inserting in {} second---", (endTime - startTime)/1000);

    }

    List<String[]> parseCSV(InputStream csvStream) throws IOException, CsvValidationException {
        List<String[]> result = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(csvStream));
            CSVReader csvReader= new CSVReaderBuilder(br)
                    .withCSVParser(
                new CSVParserBuilder()
                        .withSeparator(',')
                        .build())
        .build()
        ){

            String[] nextLine;
            while((nextLine = csvReader.readNext())!=null){
                result.add(nextLine);
            }
        }
        return result;
    }

    InputStream downloadCSV(String str) throws IOException {
        log.info("Downloading CSV file from {}", str);
        URI uri = URI.create(str);
        URL url = uri.toURL();
        HttpURLConnection conn =(HttpURLConnection) url.openConnection();
        return conn.getInputStream();
    }

    @Override
    public void run(String... args) throws Exception {
        ImportCSV();
    }
}
