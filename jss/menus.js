/*
 * Ext JS Library 2.1
 * 
 */

Ext.onReady(function(){
    Ext.QuickTips.init();

    // Menus can be prebuilt and passed by reference
    var dateMenu = new Ext.menu.DateMenu({
        handler : function(dp, date){
            Ext.example.msg('Date Selected', 'You chose {0}.', date.format('M j, Y'));
        }
    });

    var colorMenu = new Ext.menu.ColorMenu({
        handler : function(cm, color){
            Ext.example.msg('Color Selected', 'You chose {0}.', color);
        }
    });




     var menuHome = new Ext.menu.Menu({
        id: 'menuHome',
         items: [
            {
                text: 'User Profile',
                menu: {        // <-- submenu by nested config object
                    items: [
                      {
                           text: 'Change Password',
                           href :'ChangePassword.do?operation=edit&pageOf=pageOfChangePassword'
                       }
                    ]
                }
            },
              {
                text: 'Privileges',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Assign',
                            href:'AssignPrivilegesPage.do?pageOf=pageOfAssignPrivilegesPage'
                        }
                    ]
                }
            }
        ]
    });


    var menu = new Ext.menu.Menu({
        id: 'mainMenu',
        items: [
            {
                text: 'User',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
							href:'User.do?operation=add&pageOf=pageOfUserAdmin&menuSelected=1'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                              href:'SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&menuSelected=1' 
                        }, {
                            text: 'Approve New Users',
                            checked: false,
                            group: 'theme',
                              href:'ApproveUserShow.do?pageNum=1&menuSelected=1' 
                        }
                    ]
                }
            },
              {
                text: 'Institution',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'Institution.do?operation=add&pageOf=pageOfInstitution&menuSelected=2'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                           href:'SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution&menuSelected=2' 
                        }
                    ]
                }
            },
            {
                text: 'Department',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
			    href:'Department.do?operation=add&pageOf=pageOfDepartment&menuSelected=3'

                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                              href:'SimpleQueryInterface.do?pageOf=pageOfDepartment&aliasName=Department&menuSelected=3' 
                        }
                    ]
                }
            }, 
			  {
                text: 'Cancer Research Group',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'CancerResearchGroup.do?operation=add&pageOf=pageOfCancerResearchGroup&menuSelected=4'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                              href:'SimpleQueryInterface.do?pageOf=pageOfCancerResearchGroup&aliasName=CancerResearchGroup&menuSelected=4' 
                        }
                    ]
                }
            },
			  {
                text: 'Site',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'Site.do?operation=add&pageOf=pageOfSite&menuSelected=5'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                             href:'SimpleQueryInterface.do?pageOf=pageOfSite&aliasName=Site&menuSelected=5' 
                        }
                    ]
                }
            },
			  {
                text: 'Storage Type',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'StorageType.do?operation=add&pageOf=pageOfStorageType&menuSelected=6'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                              href:'SimpleQueryInterface.do?pageOf=pageOfStorageType&aliasName=StorageType&menuSelected=6' 
                        }
                    ]
                }
            },
			{
                text: 'Storage Container',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'StorageContainer.do?operation=add&pageOf=pageOfStorageContainer&menuSelected=7'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                            href:'SimpleQueryInterface.do?pageOf=pageOfStorageContainer&aliasName=StorageContainer&menuSelected=7'
                        }, {
                            text: 'View Map',
                            checked: false,
                            group: 'theme',
                            checkHandler: showStorageContainerMap
                        }
                    ]
                }
            },
			{
                text: 'Specimen Array Type',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'SpecimenArrayType.do?operation=add&amp;pageOf=pageOfSpecimenArrayType&amp;menuSelected=21'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                             href:'SimpleQueryInterface.do?pageOf=pageOfSpecimenArrayType&aliasName=SpecimenArrayType&amp;menuSelected=21' 
                        }
                    ]
                }
            },
			{
                text: 'Biohazard',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'Biohazard.do?operation=add&pageOf=pageOfBioHazard&menuSelected=8'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                              href:'SimpleQueryInterface.do?pageOf=pageOfBioHazard&aliasName=Biohazard&menuSelected=8' 
                        }
                    ]
                }
            },
			{
                text: 'Collection Protocol',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol&menuSelected=9"'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                             href:'SimpleQueryInterface.do?pageOf=pageOfCollectionProtocol&aliasName=CollectionProtocol&menuSelected=9' 
                        }
                    ]
                }
            },
			{
                text: 'Distribution Protocol',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol&menuSelected=10'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                              href:'SimpleQueryInterface.do?pageOf=pageOfDistributionProtocol&aliasName=DistributionProtocol&menuSelected=10' 
                        }
                    ]
                }
            },
			{
                text: 'Reported Problems',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'View',
                            href:'ReportedProblemShow.do?pageNum=1&menuSelected=11'
                        }
                    ]
                }
            },
			{
                text: 'Local Extensions',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Define',
                            href:'DefineAnnotationsInformationPage.do'
                        }
                    ]
                }
            },
			{
                text: 'Conflicting Reports',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'View',
                            href:'ConflictView.do?pageNum=1&menuSelected=13'
                        }
                    ]
                }
            }
			
			
//			'-', {
//                text: 'Radio Options',
//                menu: {        // <-- submenu by nested config object
//                    items: [
                        // stick any markup in a menu
//                        '<b class="menu-title">Choose a Theme</b>',
//                        {
//                            text: 'Aero Glass',
//                            checked: true,
//                            group: 'theme',
//                            checkHandler: onItemCheck
//                        }, {
//                            text: 'Vista Black',
//                            checked: false,
//                            group: 'theme',
//                            checkHandler: onItemCheck
//                        }, {
//                            text: 'Gray Theme',
//                            checked: false,
//                            group: 'theme',
//                            checkHandler: onItemCheck
//                        }, {
//                            text: 'Default Theme',
//                            checked: false,
//                            group: 'theme',
//                            checkHandler: onItemCheck
//                        }
//                    ]
//                }
//            },{
//                text: 'Choose a Color',
//                menu: colorMenu // <-- submenu by reference
//            }

		]
    });

    var menu_bio = new Ext.menu.Menu({
        id: 'menu_bio',
        items: [
            {
                text: 'Collection Protocol Based View',
                href:'CpBasedSearch.do?menuSelected=22'

            },
			{
                text: 'Participant',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'Participant.do?operation=add&pageOf=pageOfParticipant&menuSelected=12&clearConsentSession=true'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                            href:'SimpleQueryInterface.do?pageOf=pageOfParticipant&aliasName=Participant&menuSelected=12'
                        }
                    ]
                }
            },
              {
                text: 'Specimen',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                        {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                            href:'SimpleQueryInterface.do?pageOf=pageOfNewSpecimen&aliasName=Specimen&menuSelected=15'
                        }, {
                            text: 'Derive',
                            checked: false,
                            group: 'theme',
                            href:'CreateSpecimen.do?operation=add&amp;pageOf=&menuSelected=15&virtualLocated=true'
                        }, {
                            text: 'Aliquot',
                            checked: false,
                            group: 'theme',
                            href:'Aliquots.do?pageOf=pageOfAliquot&menuSelected=15'
                        }, {
                            text: 'Events',
                            checked: false,
                            group: 'theme',
                           href:'QuickEvents.do?operation=add&menuSelected=15'
                        }, {
                            text: 'Multiple',
                            checked: false,
                            group: 'theme',
                            href:'MultipleSpecimenFlexInitAction.do?pageOf=pageOfMultipleSpWithMenu'
                        }
                    ]
                }
            },
            {
                text: 'Specimen Array',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'SpecimenArray.do?operation=add&amp;pageOf=pageOfSpecimenArray&amp;menuSelected=20'
                        }, {
                            text: 'Edit',
                            checked: false,
                            group: 'theme',
                            href:'SimpleQueryInterface.do?pageOf=pageOfSpecimenArray&aliasName=SpecimenArray&amp;menuSelected=20'
                        }, {
                            text: 'Aliquot',
                            checked: false,
                            group: 'theme',
                            href:'SpecimenArrayAliquots.do?pageOf=pageOfSpecimenArrayAliquot&menuSelected=20'
                        }
                    ]
                }
            }, 
			  {
                text: 'Distribution',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Specimen Report',
                            href:'SimpleQueryInterface.do?pageOf=pageOfDistribution&aliasName=Distribution&menuSelected=16'
                        }, {
                            text: 'Array Report',
                            checked: false,
                            group: 'theme',
                            href:'SimpleQueryInterface.do?pageOf=pageOfArrayDistribution&aliasName=Distribution_array&menuSelected=16'
                        }
                    ]
                }
            },
			  {
                text: 'Order',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Order View',
                            href:'RequestListView.do?pageNum=1&menuSelected=17'
                        }
                    ]
                }
            }
        ]
    });

    var menu_search = new Ext.menu.Menu({
        id: 'menu_search',
        items: [
			{
                text: 'Saved Queries',
			    href : 'RetrieveQueryAction.do'                       
            },
            {
                text: 'Search',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Simple',
                            href:'SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface&menuSelected=17'
                        }, {
                            text: 'Advanced',
                            checked: false,
                            group: 'theme',
			    href : 'QueryWizard.do?'
                           
                        }
                    ]
                }
            },
              {
                text: 'My List',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'View',
                            href:'QueryAddToCart.do?operation=view'
                        }
                    ]
                }
            }
        ]
    });

    var menu_other = new Ext.menu.Menu({
        id: 'menu_other',
        items: [
            {
                text: 'Contact us',
                checked:false,
                href:'ContactUs.do?PAGE_TITLE_KEY=app.contactUs&FILE_NAME_KEY=app.contactUs.file'
            }, 
			  {
                text: 'Privacy Notice',
                checked:false,
                href:'PrivacyNotice.do?PAGE_TITLE_KEY=app.privacyNotice&FILE_NAME_KEY=app.privacyNotice.file'
            },
			  {
                text: 'Disclaimer',
                checked:false,
                href:'Disclaimer.do?PAGE_TITLE_KEY=app.disclaimer&FILE_NAME_KEY=app.disclaimer.file'
            },
			  {
                text: 'Accessibility',
                checked:false,
                href:'Accessibility.do?PAGE_TITLE_KEY=app.accessibility&FILE_NAME_KEY=app.accessibility.file'
            },
			  {
                text: 'Report Problems',
                checked:false,
                href:'ReportProblem.do?operation=add'
            }
        ]
    });

    

    var tb = new Ext.Toolbar();
    tb.render('toolbarLoggedIn');

    
	tb.add({ 
            text: 'Home',
			iconCls: '#',
            menu: menuHome
         },
            
		{
            text:'Administrative Data',
			iconCls: '#',  // <-- icon
			//iconCls: 'bmenu',  // <-- icon
            menu: menu  // assign menu by instance
        }, 
        {
            text:'Biospecimen Data',
            //handler: onButtonClick,
            //tooltip: {text:'This is a QuickTip with autoHide set to false and a title', title:'Tip Title', autoHide:false},
            iconCls: '#',
            // Menus can be built/referenced by using nested menu config objects
            menu: menu_bio  // assign menu by instance

        },
		{
        text: 'Search',
            iconCls: '#',  // <-- icon
			//iconCls: 'bmenu',  // <-- icon
            menu: menu_search  // assign menu by instance
    },
	{
            text:'Other Link',
            //handler: onButtonClick,
            //tooltip: {text:'This is a QuickTip with autoHide set to false and a title', title:'Tip Title', autoHide:false},
            iconCls: '#',
            // Menus can be built/referenced by using nested menu config objects
            menu: menu_other  // assign menu by instance

        });



    // functions to display feedback


    function onItemCheck(item, checked){
		alert("This Page is under construction");
      //  Ext.example.msg('Item Check', 'You {1} the "{0}" menu item.', item.text, checked ? 'checked' : 'unchecked');
    }

    function showStorageContainerMap()
    {	
	//Patch ID: Bug#4116_5
        var frameUrl='ShowFramedPage.do?pageOf=pageOfSpecimen&storageType=-1';
	//openPopupWindow(frameUrl);
	platform = navigator.platform.toLowerCase();
	if (platform.indexOf("mac") != -1)
	{
	    NewWindow(frameUrl,'name',screen.width,screen.height,'no');
	}
	else
	{
	    NewWindow(frameUrl,'name','800','600','no');
	}
     }

});