(ns bookworm-hut-frontend.translations)

(def default-locale :en-US)

(def translations
  {:en-US
   {:register
    {:title "Register"
     :username-hint "Username"
     :username-error-hint "Minimum length is 3"
     :password-hint "Password"
     :password-error-hint "Minimum length is 8"
     :submit "Register"}
    :login
    {:title "Login"
     :username-hint "Username"
     :username-error-hint "Minimum length is 3"
     :password-hint "Password"
     :password-error-hint "Minimum length is 8"
     :submit "Login"}
    :read-books
    {:add "Add book"
     :bookname-hint "Book name"
     :bookname-error-hint "Book name cannot be empty"
     :num-pages-hint "Number of pages"
     :num-pages-error-hint "Number of pages must be greater than 0"
     :month-hint "Read on month"
     :year-hint "Read on year"}
    :stats
    {:per-month "Pages read per month"
     :per-year "Pages read per year"}
    :heading
    {:read-books "Read books"
     :stats "Stats"}}
   :es-ES
   {:register
    {:title "Registrarse"
     :username-hint "Usuario"
     :username-error-hint "Longitud mínima es 3"
     :password-hint "Contraseña"
     :password-error-hint "Longitud mínima es 8"
     :submit "Registrar"}
    :login
    {:title "Iniciar sesión"
     :username-hint "Usuario"
     :username-error-hint "Longitud mínima es 3"
     :password-hint "Contraseña"
     :password-error-hint "Longitud mínima es 8"
     :submit "Acceder"}
    :read-books
    {:add "Añadir libro"
     :bookname-hint "Título"
     :bookname-error-hint "Título no puede estar vacío"
     :num-pages-hint "Número de páginas"
     :num-pages-error-hint "Número de páginas debe ser mayor a 0"
     :month-hint "Leído el mes"
     :year-hint "Leído el año"}
    :stats
    {:per-month "Páginas leídas por mes"
     :per-year "Páginas leídas por año"}
    :heading
    {:read-books "Libros leídos"
     :stats "Estadísticas"}}})

(defn tr [locale keys]
  (reduce #(%1 %2)
          (or (translations locale) (translations default-locale))
          keys))
