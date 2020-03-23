(function(window) {
	var l = 42,
		r = 10,
		w = 310,
		h = 150,
		PI = Math.PI
	var L = l + r * 2

	function getRandomNumberByRange(start, end) {
		return Math.round(Math.random() * (end - start) + start)
	}

	function createCanvas(width, height) {
		var canvas = createElement('canvas')
		canvas.width = width
		canvas.height = height
		return canvas
	}

	function createImg(onload,url) {
		var img = createElement('img')
		img.crossOrigin = "Anonymous"
		img.onload = onload;
		img.onerror = function() {
			img.src = getRandomImg(url)
		}
		img.src = getRandomImg(url)
		img.src = url;
		return img
	}

	function createElement(tagName) {
		return document.createElement(tagName)
	}

	function addClass(tag, className) {
		tag.className=className+' '+tag.className;
	}

	function removeClass(tag, className) {
		tag.className=tag.className.replace(className,'')
	}

	function getRandomImg(url) {
		return url+'?image=' + getRandomNumberByRange(0, 100)
	}

	function draw(ctx, operation, x, y) {
// 		ctx.beginPath()
// 		ctx.moveTo(x, y)
// 		ctx.lineTo(x + l / 2, y)
// 		ctx.arc(x + l / 2, y - r + 2, r, 0, 2 * PI)
// 		ctx.lineTo(x + l / 2, y)
// 		ctx.lineTo(x + l, y)
// 		ctx.lineTo(x + l, y + l / 2)
// 		ctx.arc(x + l + r - 2, y + l / 2, r, 0, 2 * PI)
// 		ctx.lineTo(x + l, y + l / 2)
// 		ctx.lineTo(x + l, y + l)
// 		ctx.lineTo(x, y + l)
// 		ctx.lineTo(x, y)
// 		ctx.fillStyle = '#fff'
// 		ctx[operation]()
// 		ctx.beginPath()
// 		ctx.arc(x, y + l / 2, r, 1.5 * PI, 0.5 * PI)
// 		ctx.globalCompositeOperation = "xor"
// 		ctx.fill()
	}

	function sum(x, y) {
		return x + y
	}

	function square(x) {
		return x * x
	}
	function Jigsaw(el, callBack, imgRequestCallBack){
		this.el = el;
		this.callBack = callBack;
		// this.bigImg = bigImg;
		// this.sonImg = sonImg;
		this.imgRequestCallBack = imgRequestCallBack;
	}
	Jigsaw.prototype={
		constructor:Jigsaw,
		init:function() {
			this.initDOM()
			// this.initImg()
			// this.draw()
			this.bindEvents()
			this.imgReady = false;
			this.imgRequestCallBack();
			return this;
		},
		initDOM:function() {
			var canvas = createCanvas(w, h)
			var block =createCanvas(L, h);
			var sliderContainer = createElement('div')
			var refreshIcon = createElement('div')
			var sliderMask = createElement('div')
			var slider = createElement('div')
			var sliderIcon = createElement('span')
			var text = createElement('span')
			block.className = 'block'
			sliderContainer.className = 'sliderContainer'
			refreshIcon.className = 'refreshIcon'
			sliderMask.className = 'sliderMask'
			slider.className = 'slider'
			sliderIcon.className = 'sliderIcon'
			text.innerHTML = '向右滑动滑块填充拼图'
			text.className = 'sliderText'
			var el = this.el
			el.appendChild(canvas)
			el.appendChild(refreshIcon)
			el.appendChild(block)
			slider.appendChild(sliderIcon)
			sliderMask.appendChild(slider)
			sliderContainer.appendChild(sliderMask)
			sliderContainer.appendChild(text)
			el.appendChild(sliderContainer)
			this.canvas=canvas;
			this.block=block;
			this.sliderContainer=sliderContainer;
			this.refreshIcon=refreshIcon;
			this.slider=slider;
			this.sliderMask=sliderMask;
			this.sliderIcon=sliderIcon;
			this.text=text;
			this.canvasCtx= canvas.getContext('2d');
			this.blockCtx= block.getContext('2d')
			// 设定初始位置
			this.intX=0;
			this.intY=0;
		},
		initImg:function(){
			var _this=this;
			var img = createImg(function(){
				_this.canvasCtx.drawImage(_this.pImg, 0, 0, w, h)
				
			},this.bigImg);
			var img2 = createImg(function(){
				_this.blockCtx.drawImage(_this.sImg, 0, 5, L, h);
				_this.blockCtx.width=L;
				
			},this.sonImg);
			this.sImg = img2;
			this.pImg = img;
		},
		handleImg: function(bigImg,sonImg){
			this.bigImg = bigImg;
			this.sonImg = sonImg;
			this.initImg();
			this.imgReady = true;
		},
		draw:function(){
			// var _this=this;
// 			this.x = getRandomNumberByRange(L + 10, w - (L + 10))
// 			this.y = getRandomNumberByRange(10 + r * 2, h - (L + 10))
// 			draw(this.canvasCtx, 'fill', this.x, this.y)
// 			draw(this.blockCtx, 'clip', this.x, this.y)
		},
		clean:function(){
			var _this=this;
			this.canvasCtx.clearRect(0, 0, w, h)
			this.blockCtx.clearRect(0, 0, L, h)
			this.block.width = L
		},
		bindEvents:function() {
			var _this=this;
			this.el.onselectstart = function(){
				return false
			}
			this.refreshIcon.onclick = function() {
				_this.resetImg()
			}
			var originX, originY, trail = [],
				isMouseDown = false;
			this.slider.addEventListener('mousedown', function(e) {
				
				if(_this.isChecking || !_this.imgReady){
					isMouseDown = false
					return false
				}
				isMouseDown = true
				originX = e.x, originY = e.y
				
				console.log(1);
			})
			document.addEventListener('mousemove', function(e) {
				// console.log('又执行到这里了');
				// console.log(isMouseDown);
				// console.log(_this.isChecking);
				if (!isMouseDown || !_this.imgReady) return false;
				if (_this.isChecking) return false;
				var moveX = e.x - originX;
				var moveY = e.y - originY;
				if (moveX+_this.intX < 0 || _this.intX+moveX + 38 >= w) return false;
				_this.slider.style.left = _this.intX+moveX + 'px';
				var blockLeft = (w - 40 - 20) / (w - 40) * moveX;
				_this.block.style.left = _this.intX+blockLeft + 'px';
				addClass(_this.sliderContainer, 'sliderContainer_active');
				_this.sliderMask.style.width =_this.intX+moveX + 'px';
				trail.push(moveY);
				
				
			})
			document.addEventListener('mouseup', function(e){
				if (!isMouseDown || !_this.imgReady) return false;
				var moveX = e.x - originX;
				var moveY = e.y - originY;
				// 重置起始值
				isMouseDown = false;
				if(this.isChecking){
					return false
				}
				_this.intX=moveX;
				_this.isChecking=true;
				if (e.x == originX) return false
				removeClass(_this.sliderContainer, 'sliderContainer_active')
				_this.trail = trail;
				var left = parseInt(_this.block.style.left)
				_this.callBack && _this.callBack(left);
				
				
			})
		}, 
		success:function(){
			this.isChecking=false;
			addClass(this.sliderContainer, 'sliderContainer_success');
			removeClass(this.sliderContainer, 'sliderContainer_fail');
		},
		fail:function(){
			this.isChecking=false;
			addClass(this.sliderContainer, 'sliderContainer_fail')
			this.text.innerHTML = '再试一次';
		},
		verify:function(){
			
		},
		reset:function(){
			
			var _this=this;
			_this.intX=0;
			this.sliderContainer.className = 'sliderContainer'
			this.slider.style.left = 0
			this.block.style.left = 0
			this.sliderMask.style.width = 0
			this.clean();
			
		},
		resetImg:function(){
			if(this.isChecking){
				return;
			}
			this.reset();
			this.isChecking=false;
			this.imgReady = false;
			this.imgRequestCallBack();
			// this.pImg.src = bigImg;
			// this.sImg.src = sonImg;
			// this.draw();
		}
	}
	window.jigsaw = {
		init: function(element, callBack,imgRequestCallBack) {
			return new Jigsaw(element, callBack,imgRequestCallBack).init();
		}
	}
}(window))
