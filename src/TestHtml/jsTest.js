//window.alert("hello js!");
// 如果 JavaScript 在 HTML 中的元素还未加载完毕时执行，会导致 document.getElementById("jsTest") 找不到元素。
window.onload = function() {
    var btnObj = document.getElementById("jsTest");
    btnObj.onclick = function () {
        //alert("js 动态注册");


        alert("js 动态注册成功, 即将跳转页面");
        // 阻止表单默认的提交行为, 使得网页可以正常跳转
        event.preventDefault();
        // 该语句会打开新标签并跳转
        window.open("https://www.baidu.com", "_blank");
        // 该语句在原页面上跳转
        //window.location.assign("https://www.baidu.com", "_blank");
    };
};
