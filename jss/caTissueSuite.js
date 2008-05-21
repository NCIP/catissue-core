// function to set focus on first element
function setFocusOnFirstElement()
	{
		frm = document.forms[0];
		if (frm != null)
		{
			for (i = 0 ; i < frm.elements.length; i++)
				{
				if (frm.elements[i].type != null &&  frm.elements[i].type != "hidden") 	
				{
					frm.elements[i].focus();	
					break;
				} 
				else {
					continue;
				}
			}
		}
	}

// function for Logout tab
	
	function MM_swapImgRestore() { 
	  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}

	function MM_findObj(n, d) { 
	  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
		d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	  if(!x && d.getElementById) x=d.getElementById(n); return x;
	}

	function MM_swapImage() { 
	  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}


function openInstitutionWindow()
{
	institutionwindow=dhtmlmodal.open('Institution', 'iframe', 'Institution.do?operation=add&pageOf=pageOfInstitution','Administrative Data', 'width=530px,height=175px,center=1,resize=0,scrolling=1')

    institutionwindow.onclose=function()
	{ 
		return true;
	}
}

function openDepartmentWindow()
{
    departmentWindow=dhtmlmodal.open('Department', 'iframe', 'Department.do?operation=add&pageOf=pageOfDepartment&menuSelected=3&submittedFor=AddNew','Administrative Data', 'width=530px,height=175px,center=1,resize=0,scrolling=1')
    
    departmentWindow.onclose=function()
	{
       return true;
	}
}

function openCRGWindow()
{
    crgWindow=dhtmlmodal.open('Cancer Research Group', 'iframe', 'CancerResearchGroup.do?operation=add&pageOf=pageOfCancerResearchGroup&menuSelected=4&submittedFor=AddNew','Administrative Data', 'width=530px,height=175px,center=1,resize=0,scrolling=1')

    crgWindow.onclose=function()
	{
	   return true;
	}
}