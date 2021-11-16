<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="labelKey"  required="true" %>
<%@ attribute  name="language" type="net.madand.conferences.entity.Language" %>
<%@ attribute  name="required" %>
<%@ attribute  name="value" %>
<%@ attribute  name="rows" %>

<c:if test="${not empty language}">
    <c:set var="name"  value="${mytl:localizedParamName(name, language)}" />
    <c:set var="afterLabelText"  value="(${language.code})" />
</c:if>

<c:set var="id"  value="${mytl:nameToID(name)}" />

<c:if test="${required}">
    <c:set var="required"  value="required" />
</c:if>

<div class="mb-3">
    <label for="${id}" class="form-label ${required}">
        <fmt:message key="${labelKey}"/>
        ${afterLabelText}
    </label>
    <textarea name="${name}" id="${id}" class="form-control" rows="${rows}"
              ${required}>${value}</textarea>
</div>
