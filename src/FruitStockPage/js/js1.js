// 当窗口加载完成后, 执行该匿名函数
window.onload = function (){
    updateZJ();
    // 当页面加载完成, 绑定各种事件
    // 根据id获取对象
    var fruitTbl = document.getElementById("tbl_fruit");
    // 获取表格中所有行
    var rows = fruitTbl.rows;
    // 对除了表头和最后一行以外进行操作
    for (var i = 1; i < rows.length - 1; i++){
        var tr = rows[i];
        // 1.绑定鼠标悬浮及离开时事件: 设置背景颜色
        tr.onmouseover = showBGColor;
        tr.onmouseout = clearBGColor;
        /* 注意: tr.onmouseover = showBGColor 仅仅是绑定该函数
           而 tr.onmouseover = showBGColor() 为绑定后在立即调用该函数 */
        // 获取tr这一行所有单元格
        var cells = tr.cells;
        var priceTD = cells[1];
        // 2.绑定鼠标悬浮在单价单元格事件: 切换光标为手势
        priceTD.onmouseover = showHand;
        var imgTD = cells[4].querySelector('IMG');
        //alert(imgTD.src);
        imgTD.onmouseover = showHand;
        // 3.绑定鼠标点击在单价单元格事件: 编辑价格
        priceTD.onclick = editPrice;

        // 7.绑定鼠标点击删除图标事件: 删除当前行并更新总计
        var img = cells[4].firstChild;
        // img.onmouseover = showHand;
        if(img && img.tagName === "IMG"){
            // 绑定单击事件
            img.onclick = deleteFruit;
        }
    }

    // 获取添加表的信息
    var addFruitTbl = document.getElementById("tbl_add_fruit");
    var rows2 = addFruitTbl.rows;
    //var nameAddTD = rows2[1].cells[1];
    var nameAddTD = document.getElementById("fName");
    var priceAddTD = document.getElementById("fPrice");
    var countAddTD = document.getElementById("fCount");
    // check
    priceAddTD.onkeydown = ckInput;
    countAddTD.onkeydown = ckInput;
    // 重置按钮
    var resetAddTD = document.getElementById("resetAdd");
    resetAddTD.onclick = function(){
        //alert(nameAddTD.type);
        nameAddTD.value = "";
        priceAddTD.value = "";
        countAddTD.value = "";
    }
    // 添加按钮
    var buttonAddTD = document.getElementById("buttonAdd");
    buttonAddTD.onclick = function(){
        //alert(nameAddTD.value);
        if(!nameAddTD.value || !priceAddTD.value || !countAddTD.value){
            alert("输入不能为空!");
        }
        else {
            var nRows2 = fruitTbl.rows.length;
            var newRow2 = fruitTbl.insertRow(nRows2 - 1);
            var cellName = newRow2.insertCell();
            cellName.innerText = nameAddTD.value;
            var cellPrice = newRow2.insertCell();
            cellPrice.innerText = priceAddTD.value;
            var cellCount = newRow2.insertCell();
            cellCount.innerText = countAddTD.value;
            var cellXJ = newRow2.insertCell();
            cellXJ.innerText = parseInt(priceAddTD.value) * parseInt(countAddTD.value);
            var cellDelImg = newRow2.insertCell();
            var DelImg = document.createElement("img");
            DelImg.src = "src/deleteImg.jpg";
            DelImg.width = 24;
            // 为新创建的图片绑定点击事件
            DelImg.onclick = deleteFruit;
            DelImg.onmouseover = showHand;
            cellDelImg.appendChild(DelImg);

            // 更新
            updateZJ();
            rows = fruitTbl.rows;
        }
    }
}


// 当鼠标悬浮时显示背景颜色
function showBGColor(){
    // event: 当前发生的事件
    // event.srcElement: 事件源
    // alert(event.srcElement);
    // alert(event.srcElement.tagName); //TD
    if(event && event.srcElement && event.srcElement.tagName=="TD"){
        var td = event.srcElement;
        // td.parentElement: 获取td父元素 --> TR
        var tr = td.parentElement;
        // 如果想通过js设置某节点的样式,则需要加上 .style
        tr.style.backgroundColor = "navy";
        // tr.cells: 获取tr中所有单元格
        var tds = tr.cells;
        for(var i = 0; i < tds.length; i++){
            tds[i].style.color = "white";
        }
    }
}

// 当鼠标离开时恢复背景颜色
function clearBGColor(){
    if(event && event.srcElement && event.srcElement.tagName=="TD"){
        var td = event.srcElement;
        var tr = td.parentElement;
        tr.style.backgroundColor = "transparent";
        var tds = tr.cells;
        for(var i = 0; i < tds.length; i++){
            tds[i].style.color = "indigo";
        }
    }
}

// 当鼠标悬浮在单价或操作单元格时, 显示手势
function showHand(){
    //alert("Mouse over detected");
    if(event && event.srcElement &&
        (event.srcElement.tagName==="TD" || event.srcElement.tagName==="IMG")){
        var td = event.srcElement;
        td.style.cursor = "pointer";
    }
}

// 当鼠标点击单价单元格事件时, 进行编辑价格
function editPrice(){
    //alert("!");
    if(event && event.srcElement){
        var priceID = event.srcElement;
        // 只有当鼠标点击到文本节点时才做以下事情
        // 判断priceID子节点存在且子节点为文本节点 (nodeType: TextNode->3 ElementNode->1 )
        if(priceID.firstChild && priceID.firstChild.nodeType === 3){
            // .innerHTML 表示获取当前节点的内部文本
            var oldPrice = priceID.innerText;
            // .innerHTML 表示设置当前节点的内部HTML
            priceID.innerHTML = "<input type='text' size='3'/>";
            var input = priceID.firstChild;
            if (input.tagName = "INPUT") {
                input.value = oldPrice;
                // 点击价格单元格时, 自动选中输入框内部文本
                input.select();
                // 4.绑定输入框失去焦点事件, 失去焦点, 更新单价
                input.onblur = updatePrice;

                // 8.在输入框上绑定键盘摁下事件: 保证用户输入为数字
                input.onkeydown = ckInput;
            }
        }

    }
}

function updatePrice(){
    if(event && event.srcElement && event.srcElement.tagName=="INPUT"){
        var input = event.srcElement;
        var newPrice = input.value;
        // input父节点是td
        var priceTD = input.parentElement;
        priceTD.innerText = newPrice;

        // 5.更新当前行的小计
        // priceTD.parentElement: td的父元素是tr, 即获取td所在的行
        updateXJ(priceTD.parentElement);
    }
}

function updateXJ(tr){
    if(tr && tr.tagName==="TR"){
        var tds = tr.cells;
        var price = tds[1].innerText;
        var count = tds[2].innerText;
        // innerText获取到的是字符串类型
        var xj = parseInt(price) * parseInt(count);
        tds[3].innerText = xj;

        // 6.更新总计
        updateZJ();
    }
}

function updateZJ(){
    var fruitTbl = document.getElementById("tbl_fruit");
    var rows = fruitTbl.rows;
    var sum = 0;
    for(var i = 1; i < rows.length - 1; i++){
        var tr = rows[i];
        var xj = parseInt(tr.cells[3].innerText);
        sum += xj;
    }
    rows[rows.length - 1].cells[1].innerText = sum;
}

function deleteFruit(){
    if(event && event.srcElement && event.srcElement.tagName=="IMG"){
        // alert只有确定, confirm有确定和取消按钮, 点确定返回true否则返回false
        if(window.confirm("是否确认删除当前行的库存记录")){
            var img = event.srcElement;
            var tr = img.parentElement.parentElement;
            var fruitTbl = document.getElementById("tbl_fruit");
            fruitTbl.deleteRow(tr.rowIndex);

            updateZJ();
        }

    }
}

function ckInput(){
    var kc = event.keyCode;
    /*
    * Ascii码值：
    * 0~9: 48~57
    * backspace: 8
    * enter: 13
    * */
    //console.log(kc);
    if(!((kc >= 48 && kc <=57) || kc===8 || kc===13)){
        // 屏蔽其他不合法的输入
        event.returnValue = false;
    }
    if(kc === 13){
        event.srcElement.blur();
    }
}

// 辅助函数：清空单元格内所有 input 元素的值
function clearInputValues(cell) {
    var inputs = cell.querySelectorAll('input');
    inputs.forEach(function(input) {
        input.value = ''; // 清空每个 input 元素的内容
    });
}