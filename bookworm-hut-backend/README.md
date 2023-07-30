# bookworm-hut-backend

## Set up local postgre sql with docker

Install docker and execute the following commands

#+begin_src bash
docker pull postgres
docker run --name bookworm_hut_postgres -e POSTGRES_USER=bookworm_hut POSTGRES_PASSWORD=bookworm_hut -d -p 5432:5432 postgres
#+end_src

Now, you can connect to the docker, create a table and see the information in the database.

#+begin_src bash
docker exec -it 312396dfc63e bash
psql -U bookworm_hut
\c bookworm_hut
create table users (id SERIAL PRIMARY KEY NOT NULL, username varchar(30), password varchar(64));
select * from users;
#+end_src

To run the backend server, from this folder run:

#+begin_src
lein repl
// Inside the repl
(start-server)
// If you modify some code, reload the file you just modified
// Examples
(use 'bookworm-hut-backend.core :reload)
(use 'bookworm-hut-backend.config.db :reload)
#+end_src