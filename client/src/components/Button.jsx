const Button = ({label,styles, rest}) => {
  return (
        <button
        {...rest}
          className={`rounded-sm border-1 border-blue-400 px-2 py-1 hover:bg-blue-400 hover:text-white cursor-pointer duration-300 ${styles}`}
        >
            {label}
        </button>
  )
}

export default Button