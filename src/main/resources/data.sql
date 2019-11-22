-- init-mode = always
truncate user_permission;
delete from user;
delete from permission;
-- default permission
insert into permission values
(1,"ROLE_USER"),
(2,"ROLE_ADMIN"),
(3,"ROLE_MODERATOR");
-- default user //password = 1
insert into user values
(1, "test1@test.pl", 1, "$2y$12$eij.BYbzAMwJBTz.WrAyt.jbw/LBuoozt968McL0w9lLlAoEq51Pe", sysdate()),
(2, "test2@test.pl", 1, "$2y$12$eij.BYbzAMwJBTz.WrAyt.jbw/LBuoozt968McL0w9lLlAoEq51Pe", sysdate()),
(3, "test3@test.pl", 1, "$2y$12$eij.BYbzAMwJBTz.WrAyt.jbw/LBuoozt968McL0w9lLlAoEq51Pe", sysdate());
-- default access
insert into user_permission values
(1,1),
(2,1),
(3,1), (3,2);