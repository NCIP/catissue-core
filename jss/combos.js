/*
 * Ext JS Library 2.0.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){
  //  Ext.QuickTips.init();

  var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: 'ComboDataAction.do'
        }),
        reader: new Ext.data.JsonReader({
            root: 'row',
            totalProperty: 'totalCount',
            id: 'id'
        }, [
              {name: 'excerpt', mapping: 'field'}
        ])
    });  

      var combo = new Ext.form.ComboBox({
        store: ds,
        displayField:'excerpt',
        typeAhead: false,
		width: 200,
	    pageSize:15,
		forceSelection: true  ,	
		queryParam : 'query',
		mode: 'remote', 
        triggerAction: 'all',
		minChars : 3,	
		queryDelay : 300,
		typeAheadDelay : 900,
        selectOnFocus:true,
        applyTo: 'clinicaldiagnosis'
   });


});