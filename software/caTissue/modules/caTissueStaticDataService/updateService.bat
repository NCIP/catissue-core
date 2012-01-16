erase /Q de_codegen\artifacts\*
erase /Q de_codegen\xmi\*
erase /Q de_codegen\xsd\*
unzip de_codegen\dynamic_extensions.zip -d de_codegen\
call ant updateService