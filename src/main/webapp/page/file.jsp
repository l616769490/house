<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/11/23
  Time: 10:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>文件上传</title>

    <link href="../assets/css/bootstrap.css" rel="stylesheet" />
    <link href="../assets/css/font-awesome.css" rel="stylesheet" />
    <link href="../assets/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <link href="../assets/css/custom-styles.css" rel="stylesheet" />
    <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-xs-4 col-md-offset-4">
            <form id="uploadForm" action="/upload" enctype="multipart/form-data" method="post">
                <div class="form-group">
                    <label for="year">文件所属年份</label>
                    <input type="text" class="form-control" id="year" name="year" placeholder="年份" required="required">
                </div>
                <div class="form-group">
                    <label for="file">选择文件</label>
                    <input type="file" id="file" name="file" accept=".csv">
                </div>
                <div class="checkbox">
                    <label>
                        <input type="checkbox" id="check" name="check"> 如果数据存在则覆盖
                    </label>
                </div>
                <button type="submit" class="btn btn-default">提交</button>
            </form>
        </div>
    </div>
</div>


<script>
    $(document).ready(function () {
        $('#uploadForm').ajaxForm(function (result) {
            alert(result);
        });
    });
</script>
</body>
</html>
