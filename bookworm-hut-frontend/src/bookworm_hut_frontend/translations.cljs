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
     :submit "Login"}}
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
     :submit "Acceder"}}
   })

(defn tr [locale keys]
  (reduce #(%1 %2)
          (or (translations locale) (translations default-locale))
          keys))
