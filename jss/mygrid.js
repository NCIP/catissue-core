		//set this variable to 1 if you wish the URLs of the highlighted menu to be displayed in the status bar
		var display_url=0
		var ie5,ns6,menuobj;

		function onBodyLoad() {
			ie5=document.all&&document.getElementById
			ns6=document.getElementById&&!document.all

			if (ie5||ns6) {
				menuobj = document.getElementById("contextmenu")
				menuobj.style.display=''
				//document.oncontextmenu=showmenuie5
				document.onclick=hidemenu
			}
		}

		function showmenu(e){
			//Find out how close the mouse is to the corner of the window
			var rightedge=ie5? document.body.clientWidth-event.clientX : window.innerWidth-e.clientX
			var bottomedge=ie5? document.body.clientHeight-event.clientY : window.innerHeight-e.clientY

			//if the horizontal distance isn't enough to accomodate the width of the context menu
			if (rightedge<menuobj.offsetWidth)
			//move the horizontal position of the menu to the left by it's width
			menuobj.style.left=ie5? document.body.scrollLeft+event.clientX-menuobj.offsetWidth : window.pageXOffset+e.clientX-menuobj.offsetWidth
			else
			//position the horizontal position of the menu where the mouse was clicked
			menuobj.style.left=ie5? document.body.scrollLeft+event.clientX : window.pageXOffset+e.clientX

			//same concept with the vertical position
			if (bottomedge<menuobj.offsetHeight)
			menuobj.style.top=ie5? document.body.scrollTop+event.clientY-menuobj.offsetHeight : window.pageYOffset+e.clientY-menuobj.offsetHeight
			else
			menuobj.style.top=ie5? document.body.scrollTop+event.clientY : window.pageYOffset+e.clientY

			menuobj.style.visibility="visible"
			return false
		}

		function hidemenu(e){
			menuobj.style.visibility="hidden"
		}

		function highlight(e){
			var firingobj=ie5? event.srcElement : e.target
			if (firingobj.className=="menuitems"||ns6&&firingobj.parentNode.className=="menuitems"){
			if (ns6&&firingobj.parentNode.className=="menuitems") firingobj=firingobj.parentNode //up one node
				//firingobj.style.backgroundColor="highlight"
				firingobj.style.backgroundColor="#def"
				firingobj.style.color="blue"
				if (display_url==1)
					window.status=event.srcElement.url
				}
		}

		function lowlight(e){
			var firingobj=ie5? event.srcElement : e.target
			if (firingobj.className=="menuitems"||ns6&&firingobj.parentNode.className=="menuitems"){
			if (ns6&&firingobj.parentNode.className=="menuitems") firingobj=firingobj.parentNode //up one node
			firingobj.style.backgroundColor=""
			firingobj.style.color="black"
			window.status=''
			}
		}

		function jumptomain(e){
			var firingobj=ie5? event.srcElement : e.target
			if (firingobj.className=="menuitems"||ns6&&firingobj.parentNode.className=="menuitems"){
			if (ns6&&firingobj.parentNode.className=="menuitems") firingobj=firingobj.parentNode
			if (firingobj.getAttribute("target"))
			window.open(firingobj.getAttribute("url"),firingobj.getAttribute("target"))
			else
			window.location=firingobj.getAttribute("url")
			}
		}