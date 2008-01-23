
/*Table structure for table de_e_1227 */

CREATE TABLE de_e_1227 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1229 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1227 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1230 */

CREATE TABLE de_e_1230 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1235 varchar(50),
  DE_AT_1234 varchar(50),
  DE_AT_1233 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1230 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1236 */

CREATE TABLE de_e_1236 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1240 date default NULL,
  DE_AT_1239 date default NULL,
  DE_AT_1238 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1230_1232_IDENTIFIER number(19,0) default NULL,
  DE_E_1264_1266_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1241 */

CREATE TABLE de_e_1241 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1243 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1241 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1241E1230 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1230 (IDENTIFIER)
) ;
/*Table structure for table de_e_1244 */

CREATE TABLE de_e_1244 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1248 varchar(50),
  DE_AT_1247 varchar(50),
  DE_AT_1246 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1249 */

CREATE TABLE de_e_1249 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1253 varchar(50),
  DE_AT_1252 varchar(50),
  DE_AT_1251 date default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1249 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1254 */

CREATE TABLE de_e_1254 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1257 varchar(50),
  DE_AT_1256 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1254 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1254E1249 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1249 (IDENTIFIER)
) ;
/*Table structure for table de_e_1258 */

CREATE TABLE de_e_1258 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1263 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1227_1260_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1264 */

CREATE TABLE de_e_1264 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1270 varchar(50),
  DE_AT_1269 varchar(50),
  DE_AT_1268 DOUBLE PRECISION default NULL,
  DE_AT_1267 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1264 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1271 */

CREATE TABLE de_e_1271 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1258_1262_IDENTIFIER number(19,0) default NULL,
  DYEXTN_AS_844_1271 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1271E1264 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1264 (IDENTIFIER)
) ;
/*Table structure for table de_e_1274 */

CREATE TABLE de_e_1274 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1258_1261_IDENTIFIER number(19,0) default NULL,
  DYEXTN_AS_844_1274 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1274E1264 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1264 (IDENTIFIER)
) ;
/*Table structure for table de_e_1277 */

CREATE TABLE de_e_1277 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1277 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1277E1254 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1254 (IDENTIFIER)
) ;
/*Table structure for table de_e_1279 */

CREATE TABLE de_e_1279 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1282 varchar(50),
  DE_AT_1281 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1279 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1279E1254 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1254 (IDENTIFIER)
) ;
/*Table structure for table de_e_1283 */

CREATE TABLE de_e_1283 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1289 varchar(50),
  DE_AT_1288 varchar(50),
  DE_AT_1287 varchar(50),
  DE_AT_1286 varchar(50),
  DE_AT_1285 date default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1283 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1290 */

CREATE TABLE de_e_1290 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1294 varchar(50),
  DE_AT_1293 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1290 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1295 */

CREATE TABLE de_e_1295 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1295 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1295E1249 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1249 (IDENTIFIER)
) ;
/*Table structure for table de_e_1297 */

CREATE TABLE de_e_1297 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1299 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1297 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1297E1230 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1230 (IDENTIFIER)
) ;
/*Table structure for table de_e_1300 */

CREATE TABLE de_e_1300 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_844_1300 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1300E1249 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1249 (IDENTIFIER)
) ;
/*Table structure for table de_e_1318 */

CREATE TABLE de_e_1318 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1321 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1322 */

CREATE TABLE de_e_1322 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1324 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1318_1320_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1325 */

CREATE TABLE de_e_1325 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1331 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1325 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1332 */

CREATE TABLE de_e_1332 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1335 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1325_1330_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1336 */

CREATE TABLE de_e_1336 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1338 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1332_1334_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1339 */

CREATE TABLE de_e_1339 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1343 varchar(50),
  DE_AT_1342 varchar(50),
  DE_AT_1341 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1325_1329_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1344 */

CREATE TABLE de_e_1344 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1348 varchar(50),
  DE_AT_1347 varchar(50),
  DE_AT_1346 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1325_1328_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1349 */

CREATE TABLE de_e_1349 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1356 varchar(50),
  DE_AT_1355 DOUBLE PRECISION default NULL,
  DE_AT_1354 number(1,0) default NULL,
  DE_AT_1353 varchar(50),
  DE_AT_1352 varchar(50),
  DE_AT_1351 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1349 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1349E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1357 */

CREATE TABLE de_e_1357 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1357 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1357E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1359 */

CREATE TABLE de_e_1359 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1362 varchar(50),
  DE_AT_1361 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1359 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1359E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1363 */

CREATE TABLE de_e_1363 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1363 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1363E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1365 */

CREATE TABLE de_e_1365 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1368 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1365 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1365E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1369 */

CREATE TABLE de_e_1369 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1374 number(19,0) default NULL,
  DE_AT_1373 number(19,0) default NULL,
  DE_AT_1372 number(19,0) default NULL,
  DE_AT_1371 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1375 */

CREATE TABLE de_e_1375 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1375 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1375E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1377 */

CREATE TABLE de_e_1377 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1380 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1377 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1377E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1381 */

CREATE TABLE de_e_1381 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1385 number(19,0) default NULL,
  DE_AT_1384 number(19,0) default NULL,
  DE_AT_1383 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1386 */

CREATE TABLE de_e_1386 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_4_1386 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1386E1325 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1325 (IDENTIFIER)
) ;
/*Table structure for table de_e_1400 */

CREATE TABLE de_e_1400 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1403 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1325_1399_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1404 */

CREATE TABLE de_e_1404 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1406 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1400_1402_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1408 */

CREATE TABLE de_e_1408 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1412 varchar(50),
  DE_AT_1411 varchar(50),
  DE_AT_1410 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1377_1407_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1414 */

CREATE TABLE de_e_1414 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1419 varchar(50),
  DE_AT_1418 varchar(50),
  DE_AT_1417 varchar(50),
  DE_AT_1416 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1365_1413_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1421 */

CREATE TABLE de_e_1421 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1425 varchar(50),
  DE_AT_1424 varchar(50),
  DE_AT_1423 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1290_1420_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1427 */

CREATE TABLE de_e_1427 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1431 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1227_1426_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1432 */

CREATE TABLE de_e_1432 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1427_1430_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1432E1274 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1274 (IDENTIFIER)
) ;
/*Table structure for table de_e_1434 */

CREATE TABLE de_e_1434 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1427_1429_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1434E1271 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1271 (IDENTIFIER)
) ;
/*Table structure for table de_e_1437 */

CREATE TABLE de_e_1437 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1439 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1620_1622_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1440 */

CREATE TABLE de_e_1440 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1446 varchar(50),
  DE_AT_1445 varchar(50),
  DE_AT_1444 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1440 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1447 */

CREATE TABLE de_e_1447 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1450 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1440_1443_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1451 */

CREATE TABLE de_e_1451 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1453 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1447_1449_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1454 */

CREATE TABLE de_e_1454 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1457 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1440_1442_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1458 */

CREATE TABLE de_e_1458 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1460 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1454_1456_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1461 */

CREATE TABLE de_e_1461 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1461 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1461E1440 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1440 (IDENTIFIER)
) ;
/*Table structure for table de_e_1468 */

CREATE TABLE de_e_1468 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1472 varchar(50),
  DE_AT_1471 varchar(50),
  DE_AT_1470 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1461_1467_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1473 */

CREATE TABLE de_e_1473 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1478 number(1,0) default NULL,
  DE_AT_1477 DOUBLE PRECISION default NULL,
  DE_AT_1476 DOUBLE PRECISION default NULL,
  DE_AT_1475 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1461_1466_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1479 */

CREATE TABLE de_e_1479 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1483 varchar(50),
  DE_AT_1482 varchar(50),
  DE_AT_1481 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1461_1465_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1484 */

CREATE TABLE de_e_1484 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1461_1464_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1489 */

CREATE TABLE de_e_1489 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1496 varchar(50),
  DE_AT_1495 number(19,0) default NULL,
  DE_AT_1494 number(19,0) default NULL,
  DE_AT_1493 number(19,0) default NULL,
  DE_AT_1492 number(19,0) default NULL,
  DE_AT_1491 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1484_1488_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1497 */

CREATE TABLE de_e_1497 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1500 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1484_1487_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1501 */

CREATE TABLE de_e_1501 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1504 varchar(50),
  DE_AT_1503 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1497_1499_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1505 */

CREATE TABLE de_e_1505 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1507 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1484_1486_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1508 */

CREATE TABLE de_e_1508 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1512 varchar(50),
  DE_AT_1511 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1461_1463_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1513 */

CREATE TABLE de_e_1513 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1515 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1508_1510_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1516 */

CREATE TABLE de_e_1516 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1516 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1516E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1518 */

CREATE TABLE de_e_1518 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1523 varchar(50),
  DE_AT_1522 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1518 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1518E1516 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1516 (IDENTIFIER)
) ;
/*Table structure for table de_e_1524 */

CREATE TABLE de_e_1524 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1526 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1518_1521_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1527 */

CREATE TABLE de_e_1527 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1532 varchar(50),
  DE_AT_1531 varchar(50),
  DE_AT_1530 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1518_1520_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1533 */

CREATE TABLE de_e_1533 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1535 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1527_1529_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1536 */

CREATE TABLE de_e_1536 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1538 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1536 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1536E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1539 */

CREATE TABLE de_e_1539 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1539 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1539E1536 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1536 (IDENTIFIER)
) ;
/*Table structure for table de_e_1541 */

CREATE TABLE de_e_1541 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1544 varchar(50),
  DE_AT_1543 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1788_1790_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1545 */

CREATE TABLE de_e_1545 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1550 DOUBLE PRECISION default NULL,
  DE_AT_1549 varchar(50),
  DE_AT_1548 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1545 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1545E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1551 */

CREATE TABLE de_e_1551 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1555 varchar(50),
  DE_AT_1554 varchar(50),
  DE_AT_1553 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1545_1547_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1556 */

CREATE TABLE de_e_1556 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1558 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1600_1602_IDENTIFIER number(19,0) default NULL,
  DE_E_1749_1751_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1559 */

CREATE TABLE de_e_1559 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1561 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1559 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1559E1516 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1516 (IDENTIFIER)
) ;
/*Table structure for table de_e_1562 */

CREATE TABLE de_e_1562 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1565 varchar(50),
  DE_AT_1564 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1615_1617_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1566 */

CREATE TABLE de_e_1566 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1569 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1797_1799_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1570 */

CREATE TABLE de_e_1570 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1573 varchar(50),
  DE_AT_1572 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1566_1568_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1574 */

CREATE TABLE de_e_1574 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1576 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1797_1800_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1577 */

CREATE TABLE de_e_1577 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1583 varchar(50),
  DE_AT_1582 varchar(50),
  DE_AT_1581 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1577 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1577E1440 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1440 (IDENTIFIER)
) ;
/*Table structure for table de_e_1584 */

CREATE TABLE de_e_1584 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1588 varchar(50),
  DE_AT_1587 varchar(50),
  DE_AT_1586 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1577_1580_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1589 */

CREATE TABLE de_e_1589 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1592 varchar(50),
  DE_AT_1591 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1577_1579_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1593 */

CREATE TABLE de_e_1593 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1599 varchar(50),
  DE_AT_1598 varchar(50),
  DE_AT_1597 varchar(50),
  DE_AT_1596 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1593 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1593E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1600 */

CREATE TABLE de_e_1600 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1604 varchar(50),
  DE_AT_1603 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1735_1747_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1605 */

CREATE TABLE de_e_1605 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1607 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1735_1746_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1608 */

CREATE TABLE de_e_1608 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1611 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1631_1633_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1612 */

CREATE TABLE de_e_1612 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1614 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1608_1610_IDENTIFIER number(19,0) default NULL,
  DE_E_1648_1650_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1615 */

CREATE TABLE de_e_1615 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1619 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1705_1707_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1620 */

CREATE TABLE de_e_1620 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1624 DOUBLE PRECISION default NULL,
  DE_AT_1623 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1615_1618_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1625 */

CREATE TABLE de_e_1625 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1630 DOUBLE PRECISION default NULL,
  DE_AT_1629 number(1,0) default NULL,
  DE_AT_1628 number(19,0) default NULL,
  DE_AT_1627 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1625 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1625E1545 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1545 (IDENTIFIER)
) ;
/*Table structure for table de_e_1631 */

CREATE TABLE de_e_1631 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1639 varchar(50),
  DE_AT_1638 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1631 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1631E1593 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1593 (IDENTIFIER)
) ;
/*Table structure for table de_e_1640 */

CREATE TABLE de_e_1640 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1643 number(1,0) default NULL,
  DE_AT_1642 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1788_1792_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1644 */

CREATE TABLE de_e_1644 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1647 varchar(50),
  DE_AT_1646 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1631_1636_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1648 */

CREATE TABLE de_e_1648 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1651 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1631_1634_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1652 */

CREATE TABLE de_e_1652 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1658 DOUBLE PRECISION default NULL,
  DE_AT_1657 DOUBLE PRECISION default NULL,
  DE_AT_1656 varchar(50),
  DE_AT_1655 number(19,0) default NULL,
  DE_AT_1654 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1652 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1652E1545 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1545 (IDENTIFIER)
) ;
/*Table structure for table de_e_1659 */

CREATE TABLE de_e_1659 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1662 varchar(50),
  DE_AT_1661 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1667_1670_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1663 */

CREATE TABLE de_e_1663 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1666 varchar(50),
  DE_AT_1665 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1680_1682_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1667 */

CREATE TABLE de_e_1667 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1672 varchar(50),
  DE_AT_1671 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1754_1758_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1673 */

CREATE TABLE de_e_1673 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1675 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1667_1669_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1676 */

CREATE TABLE de_e_1676 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1676 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1676E1545 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1545 (IDENTIFIER)
) ;
/*Table structure for table de_e_1680 */

CREATE TABLE de_e_1680 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1684 varchar(50),
  DE_AT_1683 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1676_1679_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1685 */

CREATE TABLE de_e_1685 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1689 varchar(50),
  DE_AT_1688 number(1,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1676_1678_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1690 */

CREATE TABLE de_e_1690 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1692 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1685_1687_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1693 */

CREATE TABLE de_e_1693 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1696 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1715_1718_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1697 */

CREATE TABLE de_e_1697 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1700 DOUBLE PRECISION default NULL,
  DE_AT_1699 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1693_1695_IDENTIFIER number(19,0) default NULL,
  DE_E_1723_1725_IDENTIFIER number(19,0) default NULL,
  DE_E_1727_1729_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1701 */

CREATE TABLE de_e_1701 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1704 varchar(50),
  DE_AT_1703 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1705_1708_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1705 */

CREATE TABLE de_e_1705 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1705 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1705E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1709 */

CREATE TABLE de_e_1709 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1714 varchar(50),
  DE_AT_1713 varchar(50),
  DE_AT_1712 varchar(50),
  DE_AT_1711 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1754_1759_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1715 */

CREATE TABLE de_e_1715 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1722 DOUBLE PRECISION default NULL,
  DE_AT_1721 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1715 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1715E1593 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1593 (IDENTIFIER)
) ;
/*Table structure for table de_e_1723 */

CREATE TABLE de_e_1723 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1726 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1715_1720_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1727 */

CREATE TABLE de_e_1727 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1730 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1715_1719_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1731 */

CREATE TABLE de_e_1731 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1734 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1697_1733_IDENTIFIER number(19,0) default NULL,
  DE_E_1715_1717_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1735 */

CREATE TABLE de_e_1735 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_AT_1744 varchar(50),
  DE_AT_1743 varchar(50),
  DE_AT_1742 varchar(50),
  DE_AT_1741 varchar(50),
  DE_AT_1740 number(1,0) default NULL,
  DE_AT_1739 DOUBLE PRECISION default NULL,
  DE_AT_1738 varchar(50),
  DE_AT_1737 varchar(50),
  DE_AT_1736 varchar(50),
  DYEXTN_AS_379_1735 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1735E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1749 */

CREATE TABLE de_e_1749 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1753 varchar(50),
  DE_AT_1752 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1735_1745_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1754 */

CREATE TABLE de_e_1754 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1761 varchar(50),
  DE_AT_1760 number(19,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1754 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1754E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1762 */

CREATE TABLE de_e_1762 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1764 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1754_1756_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1765 */

CREATE TABLE de_e_1765 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1769 varchar(50),
  DE_AT_1768 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1788_1793_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1770 */

CREATE TABLE de_e_1770 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1772 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1765_1767_IDENTIFIER number(19,0) default NULL,
  DE_E_1784_1786_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1773 */

CREATE TABLE de_e_1773 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1773 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1773E1461 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1461 (IDENTIFIER)
) ;
/*Table structure for table de_e_1777 */

CREATE TABLE de_e_1777 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1780 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1773_1776_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1781 */

CREATE TABLE de_e_1781 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1783 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1777_1779_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1784 */

CREATE TABLE de_e_1784 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1787 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1788_1791_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1788 */

CREATE TABLE de_e_1788 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1788 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1788E1593 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1593 (IDENTIFIER)
) ;
/*Table structure for table de_e_1794 */

CREATE TABLE de_e_1794 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1796 DOUBLE PRECISION default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1794 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1794E1545 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1545 (IDENTIFIER)
) ;
/*Table structure for table de_e_1797 */

CREATE TABLE de_e_1797 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1802 varchar(50),
  DE_AT_1801 varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DYEXTN_AS_379_1797 number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1797E1536 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1536 (IDENTIFIER)
) ;
/*Table structure for table de_e_1828 */

CREATE TABLE de_e_1828 (
  ACTIVITY_STATUS varchar(50),
  DE_AT_1833 DOUBLE PRECISION default NULL,
  DE_AT_1832 DOUBLE PRECISION default NULL,
  DE_AT_1831 DOUBLE PRECISION default NULL,
  DE_AT_1830 number(1,0) default NULL,
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1773_1827_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER)
) ;
/*Table structure for table de_e_1836 */

CREATE TABLE de_e_1836 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1754_1835_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1836E1473 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1473 (IDENTIFIER)
) ;
/*Table structure for table de_e_1838 */

CREATE TABLE de_e_1838 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1754_1834_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1838E1473 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1473 (IDENTIFIER)
) ;
/*Table structure for table de_e_1841 */

CREATE TABLE de_e_1841 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1593_1840_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1841E1473 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1473 (IDENTIFIER)
) ;
/*Table structure for table de_e_1845 */

CREATE TABLE de_e_1845 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1631_1844_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1845E1640 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1640 (IDENTIFIER)
) ;
/*Table structure for table de_e_1847 */

CREATE TABLE de_e_1847 (
  ACTIVITY_STATUS varchar(50),
  IDENTIFIER number(19,0) NOT NULL ,
  DE_E_1631_1843_IDENTIFIER number(19,0) default NULL,
  PRIMARY KEY  (IDENTIFIER),
  CONSTRAINT FKE1847E1640 FOREIGN KEY (IDENTIFIER) REFERENCES de_e_1640 (IDENTIFIER)
) ;

