/*
 * Ext JS Library 2.0.2
 * Copyright(c) 2006-2008, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://extjs.com/license
 */

Ext.onReady(function(){
   Ext.QuickTips.init();
   
   var collectionProtocolId;
   if(document.getElementById('collectionProtocolId') != null)
   	collectionProtocolId = document.getElementById('collectionProtocolId').value
  
  var myUrl= 'ComboDataAction.do?collectionProtocolId='+collectionProtocolId;
  var ds = new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: myUrl
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
		tpl: '<tpl for="."><div ext:qtip="{excerpt}" class="x-combo-list-item">{excerpt}</div></tpl>',
        applyTo: 'clinicaldiagnosis',
        fields: ['id_cp'],id: 'idjs_cp'
   });
	
  combo.on('select', function(){
			
			 var collectionProtocolId;
			   if(document.getElementById('collectionProtocolId') != null)
	   			collectionProtocolId = document.getElementById('collectionProtocolId').value
			
				//this will change rel cpe combo's url to updated colprotoid
				var appli = Ext.getCmp('idjs_cp').getValue();
				if("Show all values" == appli)
				{
					var form = Ext.getCmp('idjs_cp');
					form.store.proxy.conn.url = 'ComboDataAction.do?collectionProtocolId='+collectionProtocolId+'&showAll='+appli;
					form.store.reload();
				}
 	});
   


});