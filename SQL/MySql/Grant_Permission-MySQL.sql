GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' IDENTIFIED BY '' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '' WITH GRANT OPTION;


GRANT SELECT ON catissuecore.* TO 'adopters'@'localhost' IDENTIFIED BY 'adopters';
GRANT SELECT ON catissuecore.* TO 'adopters'@'%' IDENTIFIED BY 'adopters';

GRANT SELECT ON catissuecore_win.* TO 'adopters'@'localhost' IDENTIFIED BY 'adopters';
GRANT SELECT ON catissuecore_win.* TO 'adopters'@'%' IDENTIFIED BY 'adopters';


mysqldump -u catissue_core -p catissuecore > c:/FILE.sql


