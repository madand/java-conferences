<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="labelKey"  required="true" %>
<%@ attribute  name="entity" type="java.lang.Object" %>
<%@ attribute  name="language" type="net.madand.conferences.entity.Language" %>
<%@ attribute  name="required" type="java.lang.Boolean" %>
<%@ attribute  name="value" %>
<%@ attribute  name="type" %>

<%@ variable name-given="computedName" %>
<%@ variable name-given="computedId" %>
<%@ variable name-given="computedValue" %>
<%@ variable name-given="computedLabelText" %>
<%@ variable name-given="computedRequired" %>

<c:set var="computedName"  value="${name}" />
<c:if test="${not empty language}">
    <c:set var="computedName"  value="${mytl:localizedParamName(name, language)}" />
</c:if>

<c:set var="computedId"  value="${mytl:nameToID(computedName)}" />

<c:set var="computedValue" value="${value}" />
<c:if test="${not empty entity}">
    <c:set var="computedValue" value="${entity[name]}" />
</c:if>

<c:set var="computedLabelText">
    <fmt:message key="${labelKey}"/>

    <c:if test="${not empty language}">
        (${language.code})
    </c:if>
</c:set>

<c:if test="${required}">
    <c:set var="computedRequired"  value="required" />
</c:if>

<jsp:doBody />
