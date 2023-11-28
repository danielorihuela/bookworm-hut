(ns bookworm-hut-frontend.translations)

(def default-locale :en-US)

(def translations
  {:en-US
   {:register
    {:username-hint "Username"
     :username-error-hint "Minimum length is 3"
     :password-hint "Password"
     :password-error-hint "Minimum length is 8"
     :submit "Register"}
    :login
    {:username-hint "Username"
     :username-error-hint "Minimum length is 3"
     :password-hint "Password"
     :password-error-hint "Minimum length is 8"
     :submit "Login"}}
   :es-ES
   {:register
    {:username-hint "Usuario"
     :username-error-hint "Longitud mínima es 3"
     :password-hint "Contraseña"
     :password-error-hint "Longitud mínima es 8"
     :submit "Registrar"}
    :login
    {:username-hint "Usuario"
     :username-error-hint "Longitud mínima es 3"
     :password-hint "Contraseña"
     :password-error-hint "Longitud mínima es 8"
     :submit "Acceder"}}
   })

(defn tr [locale keys]
  (reduce #(%1 %2)
          (or (translations locale) (translations default-locale))
          keys))
