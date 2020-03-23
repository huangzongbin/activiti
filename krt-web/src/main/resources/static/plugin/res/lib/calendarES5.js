var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };
(function (wid, dcm) {
    var win = wid;
    var doc = dcm;

    function $id(id) {
        return doc.getElementById(id);
    }

    function $class(name) {
        return doc.getElementsByClassName ? doc.getElementsByClassName(name) : $(doc).find('.'+name)[0];
    }

    function loop(begin, length, fn) {
        for (var i = begin; i < length; i++) {
            if (fn(i)) break;
        }
    }

    function on(action, selector, callback) {
        doc.addEventListener(action, function (e) {
            if (selector == e.target.tagName.toLowerCase() || selector == e.target.className || selector == e.target.id) {
                callback(e);
            }
        });
    }

    function transformFormat(target, distance, time) {
        target.style.transform = 'translate3d(' + distance + 'px, 0 , 0)';
        target.style.webkitTransform = 'translate3d(' + distance + 'px, 0 , 0)';
        target.style.transition = time ? 'transform ' + time + 's ease' : 'none';
        target.style.webkitTransition = time ? 'transform ' + time + 's ease' : 'none';
    }

    function checkTime(cal) {
        var beginLength = cal.beginTime.length;
        var endLength = cal.endTime.length;
        var recentLength = cal.recentTime.length;
        if (!(beginLength === 0 || beginLength === 3)) {
            console.error('beginTime不合法 : beginTime长度为 0 或 3');
            return false;
        }
        if (!(endLength === 0 || endLength === 3)) {
            console.error('endTime不合法 : endTime长度为 0 或 3');
            return false;
        }
        if (!(recentLength === 0 || recentLength === 3)) {
            console.error('recentTime不合法 : recentLength长度为 0 或 3');
            return false;
        }
        cal.beginTime = beginLength === 3 ? cal.beginTime : [1970, 1, 1];
        cal.endTime = endLength === 3 ? cal.endTime : [new Date().getFullYear() + 1, 12, 31];
        cal.recentTime = recentLength === 3 ? cal.recentTime : [new Date().getFullYear(), new Date().getMonth() + 1, 1];
        cal.beginStamp = new Date(cal.beginTime[0], cal.beginTime[1] - 1, cal.beginTime[2]).getTime();
        cal.endStamp = new Date(cal.endTime[0], cal.endTime[1] - 1, cal.endTime[2]).getTime();
        cal.recentStamp = new Date(cal.recentTime[0], cal.recentTime[1] - 1, cal.recentTime[2]).getTime();
        cal.recentStamp < cal.beginStamp ? console.error('当前时间 recentTime 小于 开始时间 beginTime') : "";
        cal.recentStamp > cal.endStamp ? console.error('当前时间 recentTime 超过 结束时间 endTime') : "";
        return cal.beginStamp <= cal.recentStamp && cal.recentStamp <= cal.endStamp;
    }

    function checkRange(year, month, cal) {
        // 用来判断生成的月份是否超过范围
        if (!cal.isToggleBtn) return;
        $id(cal.container + 'CalendarTitleLeft').style.display = 'inline-block';
        $id(cal.container + 'CalendarTitleRight').style.display = 'inline-block';
        if (cal.canViewDisabled) return;
        if (new Date(year, month).getTime() >= new Date(cal.endTime[0], cal.endTime[1] - 1).getTime()) $id(cal.container + 'CalendarTitleRight').style.display = 'none';
        if (new Date(year, month).getTime() <= new Date(cal.beginTime[0], cal.beginTime[1] - 1).getTime()) $id(cal.container + 'CalendarTitleLeft').style.display = 'none';
    }
    function generateTitleMonth(idx, year, month, cal) {
        var monthLiLength = cal.box.querySelectorAll('.calendar-item.calendar-item' + idx)[0].querySelectorAll('li').length;
        if (monthLiLength > 35) {
            $id(cal.container).getElementsByClassName('calendar-block')[0].classList.remove('shorter');
            $id(cal.container).getElementsByClassName('calendar-block')[0].classList.add('higher');
        } else if (monthLiLength <= 28) {
            $id(cal.container).getElementsByClassName('calendar-block')[0].classList.remove('higher');
            $id(cal.container).getElementsByClassName('calendar-block')[0].classList.add('shorter');
        } else $id(cal.container).getElementsByClassName('calendar-block')[0].classList.remove('higher', 'shorter');
        var monthArr = [['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'], ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'], ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'June.', 'July.', 'Aug.', 'Sept.', 'Oct.', 'Nov.', 'Dec.'], ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']];
        return new Date(year, month).getFullYear() + ' ' + monthArr[cal.monthType][new Date(year, month).getMonth()];
    }

    function generateItemTitle(cal) {
        var chinese = '<span>一</span><span>二</span><span>三</span><span>四</span><span>五</span><span>六</span>';
        var english = '<span>M</span><span>T</span><span>W</span><span>T</span><span>F</span><span>S</span>';
        var chineseString = cal.isSundayFirst ? '<span>日</span>' + chinese : chinese + '<span>日</span>';
        var englishString = cal.isSundayFirst ? '<span>S</span>' + english : english + '<span>S</span>';
        return cal.isChinese ? '<div class="calendar-item-title">' + chineseString + '</div>' : '<div class="calendar-item-title">' + englishString + '</div>';
    }

    function generateItemBodyArr(year, month, cal) {
        // 传入计算机识别的年份和月份
        var recentArr = [];
        var dateCount = new Date(year, month + 1, 0).getDate();
        var lastDateCount = new Date(year, month, 0).getDate();
        var firstInDay = new Date(year, month, 1).getDay();
        var lastInDay = new Date(year, month + 1, 0).getDay();
        var beforeCount = cal.isSundayFirst ? firstInDay : firstInDay === 0 ? 6 : firstInDay - 1;
        var afterCount = cal.isSundayFirst ? 6 - lastInDay : lastInDay === 0 ? 0 : 7 - lastInDay;
        loop(0, beforeCount, function (i) {
            if (cal.isShowNeighbor) recentArr.unshift(lastDateCount - i + 'b');else recentArr.unshift('' + 'b');
        });
        loop(1, dateCount + 1, function (i) {
            recentArr.push(i);
        });
        loop(1, afterCount + 1, function (i) {
            if (cal.isShowNeighbor) recentArr.push(i + 'a');else recentArr.push('' + 'a');
        });
        /*console.log(' =====' + year + '年 ' + month + '月=====  ');
         console.log(recentArr);*/
        return recentArr;
    }

    function generateItemBodyDom(year, month, cal) {
        var dateArr = generateItemBodyArr(year, month, cal);
        var html = generateItemTitle(cal) + '<div style="overflow: hidden"><ul class="calendar-item-body">';
        var tempStamp = '';
        loop(0, dateArr.length, function (i) {
            if (/b$/.test(dateArr[i])) {
                html += '<li class="calendar-disabled"><i>' + dateArr[i].replace('b', '') + '</i></li>';
            } else if (/a$/.test(dateArr[i])) {
                html += '<li class="calendar-disabled"><i>' + dateArr[i].replace('a', '') + '</i></li>';
            } else {
                var lycDom = '<div class="calendar-list">';
                var tt = new Date(year, month, dateArr[i]).toLocaleDateString().replace(/\//g, "-").split("-");
                tt[1] = tt[1] < 10 ? "0" + tt[1] : tt[1];tt[2] = tt[2] < 10 ? "0" + tt[2] : tt[2];
                for (var j = 0; j < cal.monthData.length; j++) {
                    if (cal.monthData[j].logDate === tt.join("-")) {
                        if (cal.monthData[j].num1 > 0) {
                            lycDom += '<em class="num1"></em>';
                        }
                        if (cal.monthData[j].num2 > 0) {
                            lycDom += '<em class="num2"></em>';
                        }
                        if (cal.monthData[j].num3 > 0) {
                            lycDom += '<em class="num4"></em>';
                        }
                        if (cal.monthData[j].num4 > 0) {
                            lycDom += '<em class="num3"></em>';
                        }
                    }
                }
                tempStamp = new Date(year, month, dateArr[i]).getTime();
                html += '<li' + (tempStamp >= cal.beginStamp && tempStamp <= cal.endStamp ? ' class="' + cal.container + '-item-' + tempStamp + '" data-stamp="' + tempStamp + '"' : ' class="calendar-disabled"') + '><i data-stamp="' + tempStamp + '">' + dateArr[i] + '</i>' + lycDom + '</li>';
            }
        });
        return html + '</ul></div>';
    }

    function infinitePosition(cal) {
        if (cal.distance == 0) {
            transformFormat(cal.box, -3 * cal.width);
            cal.distance = -3 * cal.width;
        } else if (cal.distance == -4 * cal.width) {
            transformFormat(cal.box, -1 * cal.width);
            cal.distance = -cal.width;
        }
    }
    function switchItemBody(direct, distance, cal) {
        // direct: true 为左,direct:false为右。
        var block = $id('calendar-block-' + cal.container);
        cal.currentIdx = Math.abs(distance) % 3;
        cal.currentYear = block.querySelectorAll('.calendar-item.calendar-item' + cal.currentIdx)[0].getAttribute('data-year');
        cal.currentMonth = block.querySelectorAll('.calendar-item.calendar-item' + cal.currentIdx)[0].getAttribute('data-month') - 1;
        $id(cal.container + 'TitleCenter').innerHTML = generateTitleMonth(cal.currentIdx, cal.currentYear, cal.currentMonth, cal);

        var itemNum = direct ? (Math.abs(distance) - 1) % 3 < 0 ? 2 : (Math.abs(distance) - 1) % 3 : (Math.abs(distance) + 1) % 3;
        var applyYear = new Date(cal.currentYear, direct ? cal.currentMonth - 1 : cal.currentMonth + 1).getFullYear();
        var applyMonth = new Date(cal.currentYear, direct ? cal.currentMonth - 1 : cal.currentMonth + 1).getMonth();

        cal.box.querySelectorAll('.calendar-item.calendar-item' + itemNum).forEach(function (obj) {
            obj.innerHTML = generateItemBodyDom(cal.currentYear, direct ? cal.currentMonth - 1 : cal.currentMonth + 1, cal);
            obj.setAttribute('data-year', applyYear);
            obj.setAttribute('data-month', applyMonth + 1);
        });
        cal.switchRender(applyYear, applyMonth, cal); // 获得回调后的数组,并执行操作
    }

    function click(event, cal) {
        this.isToggleTitle ? $id('calendar-block-' + cal.container).classList.remove('calendar-block-mask-transition') : false;
        cal.start.X = event.clientX;
        cal.start.Y = event.clientY;
        cal.start.time = new Date().getTime();
        infinitePosition(cal);
        touchEnd(event, cal);
    }

    function touchMove(event, cal) {
        cal.move.Y = event.clientY;
        var offset = (cal.move.X - cal.start.X).toFixed(2);

        cal.move.increaseX = cal.move.isFirst ? event.clientX - cal.start.X : cal.move.X - cal.start.X;
        cal.move.increaseY = Math.abs(cal.move.Y - cal.start.Y);
        cal.move.S += cal.move.increaseX * cal.move.increaseY;
        cal.move.standardS += cal.move.increaseX * cal.move.increaseX * cal.move.standard;
        cal.move.X = event.clientX;
        cal.move.isFirst = false;
    }
    function touchEnd(event, cal) {
        function matchesSelector(element, selector) {
            if (element.matches) {
                return element.matches(selector);
            } else if (element.matchesSelector) {
                return element.matchesSelector(selector);
            } else if (element.webkitMatchesSelector) {
                return element.webkitMatchesSelector(selector);
            } else if (element.msMatchesSelector) {
                return element.msMatchesSelector(selector);
            } else if (element.mozMatchesSelector) {
                return element.mozMatchesSelector(selector);
            } else if (element.oMatchesSelector) {
                return element.oMatchesSelector(selector);
            }
        }
        cal.end.X = event.clientX;
        cal.end.time = new Date().getTime();
        var tempDis = (cal.end.X - cal.start.X).toFixed(2);
        if (cal.end.time - cal.start.time < 100 && Math.abs(tempDis) < 5) {
            if (matchesSelector(event.target, 'li') && event.target.className !== 'calendar-disabled' || matchesSelector(event.target,'i') && event.target.parentNode.className !== 'calendar-disabled') {
                var dataStamp = event.target.getAttribute('data-stamp');
                if (cal.resultArr.length === 0) cal.resultArr.push(dataStamp);else if (cal.resultArr.length === 1) cal.resultArr[0] < dataStamp ? cal.resultArr.push(dataStamp) : cal.resultArr.unshift(dataStamp);else {
                    cal.resultArr.length = 0;
                    cal.resultArr.push(dataStamp);
                }
                cal.success(dataStamp, cal.resultArr, cal);
            }
            transformFormat(cal.box, cal.distance, 0.5);
        }
        cal.move.X = cal.move.Y = cal.move.S = cal.move.standardS = 0;
        cal.move.isFirst = true;
        doc.body.removeEventListener('touchmove', cal.prevent, true); // 防止乱滑
    }

    function touch(event, cal) {
        event = event || window.event;
        switch (event.type) {
            case "click":
                click(event, cal);
                break;
            case "touchend":
                touchEnd(event, cal);
                break;
            case "touchmove":
                touchMove(event, cal);
                break;
        }
    }

    function Calendar(config) {
        this.clickTarget = config.clickTarget || '';
        this.container = config.container;
        this.angle = config.angle || 14;
        this.isMask = config.isMask;
        this.beginTime = config.beginTime;
        this.endTime = config.endTime;
        this.recentTime = config.recentTime;
        this.isSundayFirst = config.isSundayFirst;
        this.isShowNeighbor = config.isShowNeighbor;
        this.isToggleBtn = config.isToggleBtn;
        this.isChinese = config.isChinese;
        this.monthType = config.monthType;
        this.canViewDisabled = config.canViewDisabled;
        this.beforeRenderArr = config.beforeRenderArr || [];
        this.success = config.success;
        this.switchRender = config.switchRender;

        this.isToggleTitle = config.isToggleTitle; /* lyc by 2018.05.16*/
        this.monthData = config.monthData; /* lyc by 2018.05.25*/
        this.switchs = config.switchs;

        this.box = null;
        this.currentIdx = 2;
        this.currentYear = new Date().getFullYear();
        this.currentMonth = new Date().getMonth();

        this.width = config.width;
        this.distance = 0;

        this.beginStamp = 0;
        this.endStamp = 0;
        this.recentStamp = 0;
        this.isRangeChecked = false;
        this.resultArr = [];

        this.start = {
            X: 0,
            Y: 0,
            time: 0
        };
        this.move = {
            X: 0,
            Y: 0,
            increaseX: 0,
            increaseY: 0,
            isFirst: true,
            S: 0,
            standard: Math.tan(this.angle / 180 * Math.PI),
            standardS: 0
        };
        this.end = {
            X: 0,
            Y: 0,
            time: 0
        };
        this.initDomFuc();
        this.initReady();
        this.initBinding();
    }

    Calendar.prototype = {
        constructor: Calendar,
        initDomFuc: function initDomFuc() {
            var _this = this;
            var html = '';
            if (!checkTime(_this)) return;
            _this.currentYear = _this.recentTime[0];
            _this.currentMonth = _this.recentTime[1] - 1;

            html += _this.isMask ? '<div class="calendar-bg" id="calendar-bg-' + _this.container + '">' : '';

            html += _this.isToggleTitle ? '<div class="calendar-block' + (_this.isMask ? ' calendar-block-mask' : '') + '" id="calendar-block-' + _this.container + '">' + '<div class="calendar-title">' : '';

            html += _this.isToggleBtn ? '<span id="' + _this.container + 'CalendarTitleLeft" class="calendar-title-left"></span>' + //&#xe64f;
            '<span id="' + _this.container + 'CalendarTitleRight" class="calendar-title-right"></span>' : ''; //&#xe64e;

            html += '' + (_this.isToggleTitle ? '<b id="' + _this.container + 'TitleCenter"></b>' : '') + '</div>' + ' <div id="' + _this.container + 'Box" class="calendar-box">' + '<div class="calendar-item calendar-item0"' + ' data-year="' + new Date(_this.currentYear, _this.currentMonth + 1).getFullYear() + '"' + ' data-month="' + (new Date(_this.currentYear, _this.currentMonth + 1).getMonth() + 1) + '">' + generateItemBodyDom(_this.currentYear, _this.currentMonth + 1, _this) + '</div>' + '<div class="calendar-item calendar-item1"' + ' data-year="' + new Date(_this.currentYear, _this.currentMonth - 1).getFullYear() + '"' + ' data-month="' + (new Date(_this.currentYear, _this.currentMonth - 1).getMonth() + 1) + '">' + generateItemBodyDom(_this.currentYear, _this.currentMonth - 1, _this) + '</div>' + '<div class="calendar-item calendar-item2"' + ' data-year="' + new Date(_this.currentYear, _this.currentMonth).getFullYear() + '"' + ' data-month="' + (new Date(_this.currentYear, _this.currentMonth).getMonth() + 1) + '">' + generateItemBodyDom(_this.currentYear, _this.currentMonth, _this) + '</div>' + '<div class="calendar-item calendar-item0"' + ' data-year="' + new Date(_this.currentYear, _this.currentMonth + 1).getFullYear() + '"' + ' data-month="' + (new Date(_this.currentYear, _this.currentMonth + 1).getMonth() + 1) + '">' + generateItemBodyDom(_this.currentYear, _this.currentMonth + 1, _this) + '</div>' + '<div class="calendar-item calendar-item1"' + ' data-year="' + new Date(_this.currentYear, _this.currentMonth - 1).getFullYear() + '"' + ' data-month="' + (new Date(_this.currentYear, _this.currentMonth - 1).getMonth() + 1) + '">' + generateItemBodyDom(_this.currentYear, _this.currentMonth - 1, _this) + '</div>' + ' </div></div>';
            html += _this.isMask ? '</div>' : '';

            $id(_this.container).innerHTML = html;
            _this.box = $id(_this.container + 'Box');
            _this.renderCallbackArr(_this.beforeRenderArr);
        },
        initReady: function initReady() {
            this.box.style.transform = 'translate3d(-' + this.currentIdx * this.width + 'px, 0 , 0)';
            this.box.style.webkitTransform = 'translate3d(-' + this.currentIdx * this.width + 'px, 0 , 0)';
            this.box.style.transitionDuration = '0s';
            this.box.style.webkitTransitionDuration = '0s';
            this.distance = -this.currentIdx * this.width;
            this.isToggleTitle ? $id(this.container + 'TitleCenter').innerHTML = generateTitleMonth(this.currentIdx, this.currentYear, this.currentMonth, this) : false;
            checkRange(this.currentYear, this.currentMonth, this);
        },
        initBinding: function initBinding() {
            var _this = this;
            if (_this.isMask) {
                var bg = $id('calendar-bg-' + _this.container);
                var block = $id('calendar-block-' + _this.container);
                var body = doc.body;
                on('click', _this.clickTarget, function () {
                    bg.classList.add('calendar-bg-up', 'calendar-bg-delay');
                    block.classList.add('calendar-block-mask-up', 'calendar-block-mask-transition', 'calendar-block-action-none');
                    body.classList.add('calendar-locked');
                    body.addEventListener('touchmove', _this.prevent); // 防止乱滑
                }, false);

                on('click', 'calendar-bg-' + _this.container, function () {
                    bg.classList.remove('calendar-bg-up');
                    block.classList.remove('calendar-block-mask-up', 'calendar-block-action-none');
                    body.classList.remove('calendar-locked');
                    setTimeout(function () {
                        bg.classList.remove('calendar-bg-delay'); // 防止点透
                    }, 300);
                    body.removeEventListener('touchmove', _this.prevent);
                }, false);
            }
            this.box.addEventListener('click', function (e) {
                touch(e, _this);
            }, false);
            this.box.addEventListener('touchmove', function (e) {
                touch(e, _this);
            }, false);
            this.box.addEventListener('touchend', function (e) {
                touch(e, _this);
            }, true);
            if (_this.isToggleBtn) {
                on('click', _this.container + 'CalendarTitleLeft', function () {
                    infinitePosition(_this);
                    setTimeout(function () {
                        _this.distance = _this.distance + _this.width;
                        transformFormat(_this.box, _this.distance, .3);
                        switchItemBody(true, _this.distance / _this.width, _this);
                        checkRange(_this.currentYear, _this.currentMonth, _this);
                    }, 100);
                });
                on('click', _this.container + 'CalendarTitleRight', function () {
                    infinitePosition(_this);
                    setTimeout(function () {
                        _this.distance = _this.distance - _this.width;
                        transformFormat(_this.box, _this.distance, .3);
                        switchItemBody(false, _this.distance / _this.width, _this);
                        checkRange(_this.currentYear, _this.currentMonth, _this);
                    }, 100);
                });
            }
        },
        renderCallbackArr: function renderCallbackArr(arr) {
            var _this = this;
            loop(0, arr.length, function (k) {
                if (!$class(_this.container + '-item-' + arr[k].stamp)[0]) {
                    console.error(_this.container + '-item-' + arr[k].stamp + ' 不在范围内,请检查你的时间戳');
                    return true;
                }
                loop(0, $class(_this.container + '-item-' + arr[k].stamp).length, function (j) {
                    $class(_this.container + '-item-' + arr[k].stamp)[j].classList.add(arr[k].className);
                });
            });
        },
        prevent: function prevent(e) {
            e.preventDefault();
        },
        hideBackground: function hideBackground() {
            if (!this.isMask) return;
            var _this = this;
            var bg = $id('calendar-bg-' + _this.container);
            var block = $id('calendar-block-' + _this.container);
            var body = doc.body;
            bg.classList.remove('calendar-bg-up');
            block.classList.remove('calendar-block-mask-up', 'calendar-block-action-none');
            body.classList.remove('calendar-locked');
            setTimeout(function () {
                bg.classList.remove('calendar-bg-delay'); // 防止点透
            }, 300);
            body.removeEventListener('touchmove', _this.prevent);
        }
    };

    if ((typeof exports === 'undefined' ? 'undefined' : _typeof(exports)) == "object") {
        module.exports = Calendar;
    } else if (typeof define == "function" && define.amd) {
        define([], function () {
            return Calendar;
        });
    } else {
        win.Calendar = Calendar;
    }
})(window, document);