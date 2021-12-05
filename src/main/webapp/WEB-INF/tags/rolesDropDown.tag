<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/mytl.tld" prefix="mytl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mytags"%>

<%@ attribute  name="name"  required="true" %>
<%@ attribute  name="labelKey"  required="true" %>
<%@ attribute  name="roles" type="net.madand.conferences.auth.Role[]" required="true" %>
<%@ attribute  name="required" type="java.lang.Boolean" %>
<%@ attribute  name="value" type="net.madand.conferences.auth.Role" %>

<mytags:genericInput name="${name}" labelKey="${labelKey}" required="${required}">
    <div>
        <label for="${computedId}" class="form-label ${computedRequired}">
            ${computedLabelText}
        </label>

        <select id="${computedId}" name="${computedName}" class="form-select"
                ${computedRequired}>

            <c:forEach items="${roles}" var="role">
                <option value="${role}"
                        ${role == value ? " selected" : ""}>
                    ${role}
                </option>
            </c:forEach>
        </select>
    </div>
</mytags:genericInput>
