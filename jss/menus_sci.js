/*
 * Ext JS Library 2.1
 *
 */

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
                text: 'Advanced',
				tooltip:'Advanced Search',
			    href : 'QueryWizard.do?'
            },
            {
                text: 'My List View',
				tooltip:'My List View',
                href:'ViewCart.do?operation=view'
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

	tb.add(new Ext.Toolbar.MenuButton({text: 'Home',link:'Home.do',handler: handleMenu,menu: menuHome}),

		{
       		text: 'Search',
            menu: menu_search  // assign menu by instance
        },

		{	text:'Help',
			menu: helpMenu
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
