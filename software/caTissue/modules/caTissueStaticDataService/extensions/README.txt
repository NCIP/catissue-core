The extensions directory is to be laid out in the following order:

->extensions
	->lib (shared directory containg the jars which are required to run all extensions)
	-><extensionName (Must match the "name" attribute of your extension descriptor in the xml doc)>
		-extension.xml (xml file containing an {gme://gov.nih.nci.cagrid.introduce/1/Extension}ExtensionDescriptor element)
			- their are currently two types of extensions (service authoring extensions and types discovery extensions)
		
