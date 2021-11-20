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
<%@ attribute  name="rows" type="java.lang.Integer" %>
<%@ attribute  name="value" %>
<%@ attribute  name="type" %>

<mytags:genericInput name="${name}" labelKey="${labelKey}" entity="${entity}"
                     language="${language}" value="${value}" required="${required}">
    <div class="mb-3">
        <label for="${computedId}" class="form-label ${computedRequired}">
            ${computedLabelText}
        </label>
        <textarea name="${computedName}" id="${computedId}" class="form-control"
                  rows="${rows}" ${computedRequired}
        >${computedValue}</textarea>
    </div>
</mytags:genericInput>
