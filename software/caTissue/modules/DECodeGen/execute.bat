@echo off
echo %1 is %2
echo File: %3

ant doImportXSD -DxsdFile=%3
