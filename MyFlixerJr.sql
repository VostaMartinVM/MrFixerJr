DROP SCHEMA IF EXISTS MyFlixerJr CASCADE;
Create SCHEMA MyFlixerJr;

SET SCHEMA 'MyFlixerJr';


CREATE TABLE MyFlixerJr.GeneralUser
(
    username varchar(50) PRIMARY KEY,
    email    varchar(50),
    password varchar(50),
    role     varchar(15) not null check ( role in ('SimpleUser', 'Moderator', 'Admin'))
);

insert into MyFlixerJr.GeneralUser (username, email, password, role)
VALUES ('Admin','admin@gmail.com','admin123','Admin');

insert into MyFlixerJr.GeneralUser (username, email, password, role)
VALUES ('Moderator','admin@gmail.com','Moderator','Moderator');

drop table MyFlixerJr.GeneralUser cascade ;



CREATE TABLE MyFlixerJr.Follows
(
    usernameFollower varchar(50),
    follower         varchar(50),
    FOREIGN KEY (usernameFollower) references MyFlixerJr.GeneralUser (username),
    FOREIGN KEY (follower) references MyFlixerJr.GeneralUser (username)
);


CREATE TABLE MyFlixerJr.Review
(
    movieID int,
    username varchar(50),
    score   int check ( score in (1, 2, 3, 4, 5)),
    comment text,
    foreign key (movieID) references MyFlixerJr.movie(movieID),
    foreign key (username) references  MyFlixerJr.generaluser(username)
);

create function movie_review()
returns trigger
    language plpgsql
as $$
    begin
       MyFlixerJr.movie.averageRating = avg(score) from MyFlixerJr.Review where MyFlixerJr.review.movieid in(select movieid from MyFlixerJr.movie);
return new;
    end;

    $$

create trigger movie_rating
    before update
        on MyFlixerJr.movie
    for each row
    execute function movie_review();

update MyFlixerJr.movie
    set averagerating = 5
where movieid = 1;


DROP TABLE Review;

CREATE TABLE MyFlixerJr.Movie
(
    title         varchar(50),
    movieID serial PRIMARY KEY,
    averageRating DOUBLE PRECISION,
    actor text,
    description   text
);

insert into MyFlixerJr.movie(title, averagerating, description)
values ('xxx', 4, 'asdfdgfvscasdfvdgffvdc');

insert into MyFlixerJr.movie(title, averagerating, description)
values ('xx', 4, 'asdfdgfvscasdfvdgffvdc');
drop table MyFlixerJr.movie cascade ;

DROP TABLE MyFlixerJr.Movie;

CREATE TABLE MyFlixerJr.Image
(
    imageName text,
    img       bytea,
    movieID   int,
    FOREIGN KEY (movieID) references MyFlixerJr.Movie (movieID)
);

DROP TABLE MyFlixerJr.Image;

CREATE TABLE MyFlixerJr.GenreRelationship
(
    movieID int,
    genre   varchar(20),
    FOREIGN KEY (movieID) references MyFlixerJr.Movie (movieID),
    foreign key (genre) references MyFlixerJr.Genre(genre)

);
drop table MyFlixerJr.GenreRelationship;

create table MyFlixerJr.Genre(
    genre varchar(30) PRIMARY KEY
);

delete
from MyFlixerJr.Genre
where Genre.genre = 'comdey';




CREATE TABLE MyFlixerJr.FavoriteList
(
    movieID  int,
    username varchar(50),
    FOREIGN KEY (movieID) references MyFlixerJr.Movie (movieID),
    FOREIGN KEY (username) references MyFlixerJr.GeneralUser (username)
);

CREATE TABLE MyFlixerJr.WatchLaterList
(
    movieID  int,
    username varchar(50),
    FOREIGN KEY (movieID) references MyFlixerJr.Movie (movieID),
    FOREIGN KEY (username) references MyFlixerJr.GeneralUser (username)
);

CREATE TABLE MyFlixerJr.AlreadyWatchedList
(
    movieID  int,
    username varchar(50),
    FOREIGN KEY (movieID) references MyFlixerJr.Movie (movieID),
    FOREIGN KEY (username) references MyFlixerJr.GeneralUser (username)
);

CREATE TABLE MyFlixerJr.SelectedGenres
(
    genre varchar(20),
    username varchar(50),
    FOREIGN KEY (genre) references MyFlixerJr.Genre(genre),
    FOREIGN KEY (username) references MyFlixerJr.GeneralUser (username)
);



