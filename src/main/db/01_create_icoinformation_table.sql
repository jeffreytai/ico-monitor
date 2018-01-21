use crypto;

drop table if exists IcoInformation;

create table IcoInformation (
	Id bigint not null auto_increment,
    Name varchar(50) not null,
    CodeName varchar(50),
    Url varchar(100),
    Timestamp datetime,
    primary key (Id),
    key ix_IcoInformation_Name (Name),
    key ix_IcoInformation_CodeName (CodeName)
);

insert into IcoInformation (Name, CodeName, Url, Timestamp)
values
('GoNetwork', 'CANADA', 'https://icodrops.com/gonetwork/', now()),
('Gems', 'Twitter', 'https://icodrops.com/gems/', now());


select * from IcoInformation;