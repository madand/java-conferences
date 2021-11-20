<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="labelKey"  required="true" %>
<%@ attribute  name="entity" type="java.lang.Object" %>
<%@ attribute  name="language" type="net.madand.conferences.entity.Language" %>
<%@ attribute  name="required" %>
<%@ attribute  name="value" %>
<%@ attribute  name="type" %>

<c:if test="${not empty entity}">
    <c:set var="value" value="${entity[name]}" />
</c:if>

<c:if test="${not empty language}">
    <c:set var="name"  value="${mytl:localizedParamName(name, language)}" />
    <c:set var="afterLabelText"  value="(${language.code})" />
</c:if>

<c:set var="id"  value="${mytl:nameToID(name)}" />

<c:if test="${required}">
    <c:set var="required"  value="required" />
</c:if>

<c:if test="${empty type}">
    <c:set var="type"  value="text" />
</c:if>

<div class="mb-3">
    <label for="${id}" class="form-label ${required}">
        <fmt:message key="${labelKey}"/>
        ${afterLabelText}
    </label>
    <input name="${name}" type="${type}" value="${fn:escapeXml(value)}"
           id="${id}" class="form-control" ${required} />
</div>
