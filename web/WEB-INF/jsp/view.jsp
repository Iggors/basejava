<%@ page import="ru.basejava.model.TextSection" %>
<%@ page import="ru.basejava.model.ListSection" %>
<%@ page import="ru.basejava.model.OrganizationSection" %>
<%@ page import="ru.basejava.util.HtmlUtil" %>
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
    <div class="main-section">
        <h2>${resume.fullName}&nbsp;</h2>
        <div class="contact-section">
            <c:forEach var="contactEntry" items="${resume.contacts}">
                <jsp:useBean id="contactEntry"
                             type="java.util.Map.Entry<ru.basejava.model.ContactType, java.lang.String>"/>
                <span><%=contactEntry.getKey().toHtml(contactEntry.getValue())%></span><br/>
            </c:forEach>
        </div>
        <hr>
        <div>
            <c:forEach var="sectionEntry" items="${resume.section}">
                <jsp:useBean id="sectionEntry"
                             type="java.util.Map.Entry<ru.basejava.model.SectionType, ru.basejava.model.AbstractSection>"/>
                <c:set var="type" value="${sectionEntry.key}"/>
                <c:set var="section" value="${sectionEntry.value}"/>
                <jsp:useBean id="section" type="ru.basejava.model.AbstractSection"/>
                <div class="section-name">
                    <h2><a name="type.name">${type.title}</a></h2>
                    <hr>
                </div>
                <c:choose>
                    <c:when test="${type=='OBJECTIVE'}">
                        <span><%=((TextSection) section).getDescription()%></span>
                    </c:when>

                    <c:when test="${type=='PERSONAL'}">
                        <span <%=((TextSection) section).getDescription()%></span>
                    </c:when>

                    <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                        <ul>
                            <c:forEach var="item" items="<%=((ListSection) section).getItems()%>">
                                <li>${item}</li>
                            </c:forEach>
                        </ul>
                    </c:when>

                    <c:when test="${type=='EXPERIENCE' || type=='EDUCATION'}">
                        <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>">
                            <table>
                                <tr>
                                    <td colspan="2">
                                        <c:choose>
                                            <c:when test="${empty org.homePage.url}">
                                                <h3>${org.homePage.name}</h3>
                                            </c:when>
                                            <c:otherwise>
                                                <h3><a href="${org.homePage.url}">${org.homePage.name}</a></h3>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </table>
                            <c:forEach var="position" items="${org.positions}">
                                <jsp:useBean id="position" type="ru.basejava.model.Organization.Position"/>
                                <table>
                                    <tr>
                                        <td width="15%" style="vertical-align: top"><%=HtmlUtil.formatDates(position)%></td>
                                        <td><b>${position.title}</b><br>${position.description}</td>
                                    </tr>
                                </table>
                            </c:forEach>
                        </c:forEach>
                    </c:when>

                </c:choose>
            </c:forEach>
        </div>
        <br/>
        <button onclick="window.history.back()">ОК</button>
    </div>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>