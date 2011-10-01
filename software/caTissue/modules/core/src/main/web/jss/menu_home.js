/*
 * Ext JS Library 2.1
 *
 */

Ext.onReady(function(){
    Ext.QuickTips.init();

    var tb = new Ext.Toolbar();
    tb.render('toolbarLoggedOut');
    tb.add(new Ext.Toolbar.Button({text: 'Home',link:'CasLogin.do',handler: handleMenu}));

    // functions to display feedback

	function handleMenu(item)
	{
	document.location.href = item.link;
	}

});