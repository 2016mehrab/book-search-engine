package com.samurai74.booksearchengine.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name="books")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private BigDecimal rating;
    @Column(columnDefinition ="TEXT" ,nullable = false)
    private String description;
    @Column(nullable = false)
    private String language;

    @Column(name = "pages", nullable = false)
    private int pages;

    @NaturalId
    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false, name = "book_format")
    private String bookFormat;
    @Column(nullable = false, name = "publisher")
    private String publisher;
    @Column(nullable = false, name = "edition")
    private String edition;
    @Column(nullable = false, name = "publish_date")
    private LocalDate publishDate;
    @Column(nullable = false, name = "first_publish_date")
    private LocalDate firstPublishDate;
    @Column(nullable = false, name = "liked_percent")
    private BigDecimal likedPercent;
    @Column(nullable = false)
    private BigDecimal price;


    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "books_authors",
            joinColumns = @JoinColumn(
                    name = "book_id",
                    referencedColumnName = "book_id",
                    foreignKey = @ForeignKey(name = "FK_books_authors_to_books")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "author_id",
                    referencedColumnName = "author_id",
                    foreignKey = @ForeignKey(name = "FK_books_authors_to_authors")
            )
            )
    private Set<Author> authors;

    public  void addAuthor(Author author){
        authors.add(author);
        author.getBooks().add(this);
    }

    public  void removeAuthor(Author author){
        authors.remove(author);
        author.getBooks().remove(this);
    }
}
