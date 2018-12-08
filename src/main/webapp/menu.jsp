<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>后台管理系统</title>
    <link href="/css/frame.css" rel="Stylesheet" type="text/css"/>
    <link href="/css/menu.css" rel="Stylesheet" type="text/css"/>
    <script src="/js/jquery-1.12.4.min.js"></script>
    <style type="text/css">
        body
        {
            margin: 0;
            padding: 0;
            border: 0;
            overflow: hidden;
            height: 100%;
            max-height: 100%;
        }

        li{

            list-style: none;
        }

        .accordion {
            width: 100%;
            max-width: 360px;
            margin: 30px 10px 20px;
            padding-left: 10px;
            background: #FFF;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
        }

        .accordion .link {
            cursor: pointer;
            display: block;
            padding: 15px 15px 15px 5px;
            color: #4D4D4D;
            font-size: 14px;
            font-weight: 700;
            border-bottom: 1px solid #CCC;
            position: relative;
            -webkit-transition: all 0.4s ease;
            -o-transition: all 0.4s ease;
            transition: all 0.4s ease;
        }

        .accordion li:last-child .link {
            border-bottom: 0;
        }

        .accordion li i {
            position: absolute;
            top: 16px;
            left: 12px;
            font-size: 18px;
            color: #595959;
            -webkit-transition: all 0.4s ease;
            -o-transition: all 0.4s ease;
            transition: all 0.4s ease;
        }

        .accordion li i.fa-chevron-down {
            right: 12px;
            left: auto;
            font-size: 16px;
        }

        .accordion li.open .link {
            color: #b63b4d;
        }

        .accordion li.open i {
            color: #b63b4d;
        }
        .accordion li.open i.fa-chevron-down {
            -webkit-transform: rotate(180deg);
            -ms-transform: rotate(180deg);
            -o-transform: rotate(180deg);
            transform: rotate(180deg);
        }

        /**
         * Submenu
         -----------------------------*/
        .submenu {
            display: none;
            background: #444359;
            font-size: 14px;
            padding-left: 10px;
        }

        .submenu li {
            border-bottom: 1px solid #4b4a5e;
        }

        .submenu a {
            display: block;
            text-decoration: none;
            color: #d9d9d9;
            padding: 12px;
            padding-left: 10px;
            -webkit-transition: all 0.25s ease;
            -o-transition: all 0.25s ease;
            transition: all 0.25s ease;
        }

        .submenu a:hover {
            background: #b63b4d;
            color: #FFF;
        }

    </style>



</head>
<body>
<form id="form1" runat="server">
    <div id="framecontentLeft">
        <ul id="accordion" class="accordion">

        </ul>
    </div>

    <div id="framecontentTop">
        <div style="text-align: center;">

            <h1>
                美国房价分析
            </h1>
        </div>
    </div>
    <div id="maincontent" style="overflow: hidden">
        <iframe id="content" name="content"  frameborder="0" scrolling="auto" width="100%" height="100%"></iframe>
    </div>
</form>
</body>
</html>
<script>
    $(function (){
        $.ajax({
            url: "/testMenu",
            type: "POST",

            success: function (result) {
                // alert("1");
                var accordion =  $("#accordion");

                var groupArray = result.group;
                var year = result.year;

                for(var i =0;i<groupArray.length ; i++){
                    var group = groupArray[i];
                    var groupLi = $("<li></li>");
                    var groupName =$("<div class='link'></div>").append(group.name);
                    groupLi.append(groupName);
                    var submenu = $("<ul class='submenu'></ul>");

                    var submenuArry = groupArray[i].report;
                    for(var j =0 ;j <submenuArry.length;j++ ){
                        submenu.append("<li><a href='/index.jsp?url="+ submenuArry[i].url+"&year="+year+"' target='content' '>" +submenuArry[i].name+ "</a></li>");
                    }
                    groupLi.append(submenu);
                    accordion.append(groupLi);
                }
                var accordion = new Accordion($('#accordion'), false);

            },
            error:function (e,e1) {
                console.log(e);
                console.log(e1);
            }
        });

        var Accordion = function(el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;

            // Variables privadas
            var links = this.el.find('.link');
            // Evento
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown)
        }

        Accordion.prototype.dropdown = function(e) {
            var $el = e.data.el;
            $this = $(this),
                $next = $this.next();

            $next.slideToggle();
            $this.parent().toggleClass('open');

            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp().parent().removeClass('open');
            };
        }


    });

    function  sendUrl( url) {
               // alert(url);
            //document.getElementById("content").src="index.jsp?url=" + url;

    }
</script>