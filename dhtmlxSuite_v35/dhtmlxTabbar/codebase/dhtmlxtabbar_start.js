//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/
function dhx_init_tabbars(){for(var h=document.getElementsByTagName("div"),g=0;g<h.length;g++)if(h[g].className.indexOf("dhtmlxTabBar")!=-1){var a=h[g],j=a.id;a.className="";for(var f=[],e=0;e<a.childNodes.length;e++)a.childNodes[e].tagName&&a.childNodes[e].tagName!="!"&&(f[f.length]=a.childNodes[e]);var c=new dhtmlXTabBar(j,a.getAttribute("mode")||"top",a.getAttribute("tabheight")||20);window[j]=c;(b=a.getAttribute("onbeforeinit"))&&eval(b);a.getAttribute("enableForceHiding")&&c.enableForceHiding(!0);
c.setImagePath(a.getAttribute("imgpath"));var b=a.getAttribute("margin");if(b!=null)c._margin=b;if(b=a.getAttribute("align"))c._align=b;(b=a.getAttribute("hrefmode"))&&c.setHrefMode(b);b=a.getAttribute("offset");if(b!=null)c._offset=b;b=a.getAttribute("tabstyle");b!=null&&c.setStyle(b);var b=a.getAttribute("select"),i=a.getAttribute("skinColors");i&&c.setSkinColors(i.split(",")[0],i.split(",")[1]);for(e=0;e<f.length;e++){var d=f[e];d.parentNode.removeChild(d);c.addTab(d.id,d.getAttribute("name"),
d.getAttribute("width"),null,d.getAttribute("row"));var k=d.getAttribute("href");k?c.setContentHref(d.id,k):c.setContent(d.id,d);if(!c._dspN&&d.style.display=="none")d.style.display=""}f.length&&c.setTabActive(b||f[0].id);(b=a.getAttribute("oninit"))&&eval(b)}}dhtmlxEvent(window,"load",dhx_init_tabbars);

//v.3.5 build 120822

/*
Copyright DHTMLX LTD. http://www.dhtmlx.com
To use this component please contact sales@dhtmlx.com to obtain license
*/