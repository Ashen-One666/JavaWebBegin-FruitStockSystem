function delFruit(fid){
    if(confirm('是否确认删除？')){
        window.location.href="fruit.do?fid=" + fid + "&operate=del";
    }
}

function page(pageNo){
    // 跳转到IndexServlet，在该servlet中会将pageNo保存到session作用域作为记录
    // 下次翻页时index.html中读取到的pageNo就是上次保存在session中的pageNo，之后将以记录在session中的pageNo为基础进行翻页
    // 没写operate默认index方法
    window.location.href="fruit.do?pageNo=" + pageNo;
}