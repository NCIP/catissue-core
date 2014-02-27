/*
 * Ext JS Library 2.1
 *
 */
var userName = document.getElementById('tmp_usrName').value;


Ext.onReady(function(){
    Ext.QuickTips.init();

	// for toolTip
	Ext.menu.BaseItem.prototype.onRender = function(container){
		this.el = Ext.get(this.el);
		container.dom.appendChild(this.el.dom);
		if (this.tooltip) {
		this.el.dom.qtip = this.tooltip;
		}
	};
	
	// end Here


    // Menus can be prebuilt and passed by reference
    var menuHome = new Ext.menu.Menu({
        id: 'menuHome',
         items: [
			{
                text: 'My Profile',
				tooltip:'My Profile',
				 handler: editUserProfile
			},
			{
                text: 'Change Password',
				tooltip:'Change Password',
			    href :'ChangePassword.do?operation=edit&pageOf=pageOfChangePassword'
            }
        ]
    });

    var menu = new Ext.menu.Menu({
        id: 'mainMenu',
        items: [
            {
                text: 'User',
				href:'User.do?operation=add&pageOf=pageOfUserAdmin',
				tooltip:'Add User',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
							href:'User.do?operation=add&pageOf=pageOfUserAdmin'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfUserAdmin&aliasName=User&isForm=true'
                        }, {
                            text: 'Approve New Users',
                            href:'ApproveUserShow.do?pageNum=1'
	                    }, {
                            text: 'Login Dashboard',
                            href:'LoginAuditDashboard.do?operation=init&recordPerPage=200000&startIndex=1'
	                       }
                    ]
                }
            },
				{
                text: 'Institution',
				tooltip:'Add Institution',
				href:'Institution.do?operation=add&pageOf=pageOfInstitution',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
							href:'Institution.do?operation=add&pageOf=pageOfInstitution'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfInstitution&aliasName=Institution&isForm=true'
                        }
                    ]
                }
            },
				{
                text: 'Department',
				tooltip:'Add Department',
				href:'Department.do?operation=add&pageOf=pageOfDepartment',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
							href:'Department.do?operation=add&pageOf=pageOfDepartment'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfDepartment&aliasName=Department&isForm=true'
                        }
                    ]
                }
            },
				{
                text: 'Cancer Research Group',
				tooltip:'Add Cancer Research Group',
				href:'CancerResearchGroup.do?operation=add&pageOf=pageOfCancerResearchGroup',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
							href:'CancerResearchGroup.do?operation=add&pageOf=pageOfCancerResearchGroup'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfCancerResearchGroup&aliasName=CancerResearchGroup&isForm=true'
                        }
                    ]
                }
            },

			  {
                text: 'Site',
				tooltip:'Add Site',
				href:'Site.do?operation=add&pageOf=pageOfSite',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'Site.do?operation=add&pageOf=pageOfSite'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfSite&aliasName=Site&isForm=true'
                        }
                    ]
                }
            },
			  {
                text: 'Storage Type',
				tooltip:'Add Storage Type',
				href:'StorageType.do?operation=add&pageOf=pageOfStorageType',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                             href:'StorageType.do?operation=add&pageOf=pageOfStorageType'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfStorageType&aliasName=StorageType'
                        }
                    ]
                }
            },
			{
                text: 'Storage Container',
				tooltip:'Add Storage Container',
				href:'OpenStorageContainer.do?operation=add&pageOf=pageOfStorageContainer',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'OpenStorageContainer.do?operation=add&pageOf=pageOfStorageContainer'
                        }, {
                            text: 'Edit',
                             href:'SimpleQueryInterface.do?pageOf=pageOfStorageContainer&aliasName=StorageContainer'
                        }, {
                            text: 'View Map',
                             href:'OpenStorageContainer.do?operation=showEditAPageAndMap&pageOf=pageOfStorageContainer'
                        }
                    ]
                }
            },
			{
                text: 'Specimen Array Type',
				tooltip:'Add Specimen Array Type',
				href:'SpecimenArrayType.do?operation=add&amp;pageOf=pageOfSpecimenArrayType',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'SpecimenArrayType.do?operation=add&amp;pageOf=pageOfSpecimenArrayType'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfSpecimenArrayType&aliasName=SpecimenArrayType'
                        }
                    ]
                }
            },
			{
                text: 'Biohazard',
				tooltip:'Add Biohazard',
				href:'Biohazard.do?operation=add&pageOf=pageOfBioHazard',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'Biohazard.do?operation=add&pageOf=pageOfBioHazard'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfBioHazard&aliasName=Biohazard&isForm=true'
                        }
                    ]
                }
            },
			{
                text: 'Collection Protocol',
				tooltip:'Add Collection Protocol',
//				href:'CollectionProtocol.do?operation=add&pageOf=pageOfCollectionProtocol',
					                href:'OpenCollectionProtocol.do?pageOf=pageOfmainCP&operation=add',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'OpenCollectionProtocol.do?pageOf=pageOfmainCP&operation=add'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfCollectionProtocol&aliasName=CollectionProtocol'
                        }
                    ]
                }
            },
			{
                text: 'Distribution Protocol',
				tooltip:'Add Distribution Protocol',
				href:'DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'DistributionProtocol.do?operation=add&pageOf=pageOfDistributionProtocol'
                        }, {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfDistributionProtocol&aliasName=DistributionProtocol'
                        }
                    ]
                }
            },
			{
                text: 'Local Extensions',
				tooltip:'Shows Local Extensions',
                href:'DefineAnnotationsInformationPage.do?operation=add',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                        {
                            text: 'Add',
                            href:'DefineAnnotationsInformationPage.do?operation=add'
                        }, {
                            text: 'Edit',
                            href:'DefineAnnotationsInformationPage.do?operation=edit'
                        }
                    ]
                }
            },
            {
                text: 'Reported Problems',
				tooltip:'Shows Reported Problems',
                href:'ReportedProblemShow.do?pageNum=1'
            },
			{
                text: 'Conflicting SPRs',
				tooltip:'Shows Conflicting Surgical Pathology Reports',
                href:'ConflictView.do?pageNum=1'
            }
		]
    });
    
    
			// Add this item to menu if EMPI is enabled	
			if(empiEnabled=='true')
			{
				var boItem=new Ext.menu.Item({
							text: 'Bulk EMPI Operations',
							tooltip:'Bulk EMPI Opearations',
							href:'BulkEmpiGeneration.do?'
						});
			
				var item = menu.add(boItem);
			}			
			
			
            // For Bio Specimen Data
    var menu_ng = new Ext.menu.Menu({
        id: 'menu_ng',
        items: [
          {
            text: 'Dynamic Extensions',
            tooltip: 'Dynamic Extensions',
            href: 'ecrf.do'
          },
          {
            text: 'Query',
            tooltip: 'Query',
            href: 'query.do'
          }
        ]
    });

    var menu_bio = new Ext.menu.Menu({
        id: 'menu_bio',
        items: [
            {
                text: 'Collection Protocol Based View',
				tooltip:'Collection Protocol Based View',
                href:'CpBasedSearch.do'

            },

              {
                text: 'Specimen',
				tooltip:'Edit Specimen',
				href:'SimpleQueryInterface.do?pageOf=pageOfNewSpecimen&aliasName=Specimen',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                        {
                            text: 'Edit',
                            href:'SimpleQueryInterface.do?pageOf=pageOfNewSpecimen&aliasName=Specimen'
                        }, {
                            text: 'Derive',
                            href:'CreateSpecimen.do?operation=add&pageOf=pageOfDeriveSpecimen&virtualLocated=true'
                        }, {
                            text: 'Aliquot',
                            href:'GetAliquotDetails.do?pageOf=fromMenu&parentSpecimentLabel=&aliquotCount=&quantityPerAliquot=&searchBasedOn=label'
                        }, {
                            text: 'Events',
                            href:'QuickEvents.do?operation=add'
                        }, {
                            text: 'Multiple',
                             href:'MultipleSpecimenFlexInitAction.do?pageOf=pageOfMultipleSpWithMenu'
                        }
                    ]
                }
            },
            {
                text: 'Specimen Array',
				tooltip:'Add Specimen Array',
				href:'SpecimenArray.do?operation=add&amp;pageOf=pageOfSpecimenArray',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Add',
                            href:'SpecimenArray.do?operation=add&amp;pageOf=pageOfSpecimenArray'
                        }, {
                            text: 'Edit',
                             href:'SimpleQueryInterface.do?pageOf=pageOfSpecimenArray&aliasName=SpecimenArray'
                        }, {
                            text: 'Aliquot',
                            href:'SpecimenArrayAliquots.do?pageOf=pageOfSpecimenArrayAliquot'
                        }
                    ]
                }
            },
			  {
                text: 'Distribution',
				tooltip:'Specimen Report',
				href:'SimpleQueryInterface.do?pageOf=pageOfDistribution&aliasName=Distribution',
                menu: {        // <-- submenu by nested config object
                    items: [
                        // stick any markup in a menu
                       {
                            text: 'Specimen Report',
                            href:'SimpleQueryInterface.do?pageOf=pageOfDistribution&aliasName=Distribution'
                        }, {
                            text: 'Array Report',
                            href:'SimpleQueryInterface.do?pageOf=pageOfArrayDistribution&aliasName=Distribution_array'
                        }
                    ]
                }
            },

                                      {

                            text: 'Order View',
							tooltip:'Order View',
		                    href:'RequestListView.do?pageNum=1'

            },
            {
                text: 'Shipping And Tracking',
				tooltip:'Shipping And Tracking Dashboard',
				href : 'ShowDashboardAction.do'
            }
        ]
    });
    
	
    if(empiEnabled=='true')
    {
    	var processMessageItem=new Ext.menu.Item({
							text: 'Process Messages',
							tooltip:'Process Messages',
							href:'ProcessMatchedParticipants.do?pageOf=pageOfMatchedParticipant&identifierFieldIndex=0',
							menu: {        // <-- submenu by nested config object
								items: [
									// stick any markup in a menu
								   {
										text: 'Registration',
										href:'ProcessMatchedParticipants.do?pageOf=pageOfMatchedParticipant&identifierFieldIndex=0'
								   }
								]
							}
						});
		var item = menu_bio.add(processMessageItem);
    }
            // For Search link

    var menu_search = new Ext.menu.Menu({
        id: 'menu_search',
        items: [
			{
                text: 'Saved Queries',
				tooltip:'Saved Queries',
			    href : 'ShowQueryDashboardAction.do'
            },
			{
                text: 'Simple',
				tooltip:'Simple Search',
			    href : 'SimpleQueryInterface.do?pageOf=pageOfSimpleQueryInterface'
            },
            {
                text: 'My List View',
				tooltip:'My List View',
                href:'ViewCart.do?operation=view'
            },
            {
                text: 'Specimen List View',
				tooltip:'Specimen List View',
                href:'ViewSpecimenList.do?operation=view'
            }
        ]
    });

	    var logout_menu = new Ext.menu.Menu({
        id: 'logout_menu',
		items: [
			{
                text: 'My Profile',
				tooltip:'My Profile',
				 handler: editUserProfile
			},
			{
                text: 'Change Password',
				tooltip:'Change Password',
			    href :'ChangePassword.do?operation=edit&pageOf=pageOfChangePassword'
            },
			{
				text: 'Help Home',
				href:'/catissuecore/RedirectToHelp.do',
				tooltip:'Help Home'
			},
			{
				text: 'User Guide',
				href: '#',
				handler: getUserGuideLink,
				tooltip: 'User Guide'
			},
			{
				text: 'UML Model',
				href:'#',
				handler: getUmlModelLink,
				tooltip:'UML Model'
			},
			
			{
				text: 'Report Problem',
				href:'ReportProblem.do?operation=add', 
				tooltip:'Report Problem'
			},
			
			{
				text: 'Data Summary',
				href:'Summary.do', 
				tooltip:'Data Summary'
			},
			
			{
				text: 'Logout',
				href:'/catissuecore/Logout.do', 
				tooltip:'Log Out'
			}
		]
    });

	
	
	
	

	//For Help Tab

	var helpMenu = new Ext.menu.Menu({
        id: 'helpMenu',
         items: [
			{
				text: 'Help Home',
				href:'/catissuecore/RedirectToHelp.do',
				tooltip:'Help Home'
			},
			{
				text: 'User Guide',
				href: '#',
				handler: getUserGuideLink,
				tooltip: 'User Guide'
			},
			{
				text: 'UML Model',
				href:'#',
				handler: getUmlModelLink,
				tooltip:'UML Model'
			}
        ]
    });

    var tb = new Ext.Toolbar();
    tb.render('toolbarLoggedIn');

		tb.add(
                {
                   text: 'NextGen',
                   menu: menu_ng
                },

                new Ext.Toolbar.MenuButton(
		{
			text:'Administration',
            menu: menu 
		}),

        {
            text:'Biobanking',
            menu: menu_bio  // assign menu by instance
        },
		{
       		text: 'Search',
            menu: menu_search  // assign menu by instance
        },
		
		{	
			text: document.getElementById('tmp_usrName').value,
			menu: logout_menu
		},
		
		{	
			text: '<img src="images/help.png"/>',
			href:'#',
			handler: getHelpURL
			//tooltip:'help'
		}
		);

    // functions to display feedback


    function onItemCheck(){
		alert("This Page is under construction");
      //  Ext.example.msg('Item Check', 'You {1} the "{0}" menu item.', item.text, checked ? 'checked' : 'unchecked');
    }
	function handleMenu(item)
	{
	document.location.href = item.link;
	}

});
