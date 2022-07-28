<%@ page import="ru.basejava.model.ContactType" %>
<%@ page import="ru.basejava.model.SectionType" %>
<%@ page import="ru.basejava.model.ListSection" %>
<%@ page import="ru.basejava.model.OrganizationSection" %>
<%@ page import="ru.basejava.util.DateUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="ru.basejava.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">

        <input class="form-input-text" type="hidden" name="uuid" value="${resume.uuid}">
        <h2><a>Персональная информация</a></h2>
        <hr>
        <dl>
            <dt>ФИО:</dt>
            <dd><input required class="form-input-text" type="text" name="fullName" pattern="[\S][A-Za-zА-Яа-яЁё\s]*"
                       title="Имя не должно содержать цифр" size=30 value="${resume.fullName}"></dd>
        </dl>

        <h2><a>Контакты</a></h2>
        <hr>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input class="form-input-text" type="text" name="${type.name()}" size=30
                           value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>

        <div class="section-name">
            <c:forEach var="type" items="<%=SectionType.values()%>">
                <c:set var="section" value="${resume.getSection(type)}"/>
                <jsp:useBean id="section" type="ru.basejava.model.AbstractSection"/>
                <h2><a>${type.title}</a></h2>
                <hr>
                <c:choose>
                    <c:when test="${type=='OBJECTIVE'}">
                        <textarea class="form-input-text textarea-sized-rows" name='${type}' cols=75 rows=5><%=section%></textarea>
                    </c:when>

                    <c:when test="${type=='PERSONAL'}">
                        <textarea class="form-input-text" name='${type}' cols=75 rows=5><%=section%></textarea>
                    </c:when>

                    <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                        <textarea class="form-input-text" name='${type}' cols=75
                                  rows=5><%=String.join("\n", ((ListSection) section).getItems())%></textarea>
                    </c:when>

                    <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                        <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                                   varStatus="counter">
                            <dl>
                                <dt>Организация:</dt>
                                <dd><input class="form-input-text" type="text" name='${type}' size=100
                                           value="${org.homePage.name}"></dd>
                            </dl>
                            <dl>
                                <dt>Web-cайт организации:</dt>
                                <dd><input class="form-input-text" type="text" name='${type}url' size=100
                                           value="${org.homePage.url}"></dd>
                            </dl>
                            <br>
                            <div style="margin-left: 30px">
                                <c:forEach var="pos" items="${org.positions}">
                                    <jsp:useBean id="pos" type="ru.basejava.model.Organization.Position"/>
                                    <dl>
                                        <dt>Начало работы:</dt>
                                        <dd>
                                            <input class="form-input-text" type="text"
                                                   name="${type}${counter.index}startDate" size=10
                                                   value="<%=DateUtil.format(pos.getStartDate())%>"
                                                   placeholder="MM/yyyy">
                                        </dd>
                                    </dl>
                                    <dl>
                                        <dt>Окончание:</dt>
                                        <dd>
                                            <input class="form-input-text" type="text"
                                                   name="${type}${counter.index}endDate" size=10
                                                   value="<%=DateUtil.format(pos.getEndDate())%>" placeholder="MM/yyyy">
                                    </dl>
                                    <dl>
                                        <dt>Должность:</dt>
                                        <dd><input  class="form-input-text" type="text"
                                                   name='${type}${counter.index}title' size=75
                                                   value="${pos.title}">
                                    </dl>
                                    <dl>
                                        <dt>Обязанности и достижения на рабочем месте:</dt>
                                        <dd><textarea class="form-input-text" name="${type}${counter.index}description"
                                                      rows=5
                                                      cols=75>${pos.description}</textarea></dd>
                                    </dl>
                                </c:forEach>
                            </div>
                        </c:forEach>
                    </c:when>
                </c:choose>
            </c:forEach>
        </div>
        <hr>
        <button class="button button-OK" type="submit">Сохранить</button>
        <button class="button button button-cancel" type="reset" onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
