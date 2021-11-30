<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="user.login.title"/>
</c:set>
<%@ include file="/WEB-INF/jspf/head.jspf" %>

<div class="row">
    <div class="col-sm-4">
        <form method="post" action="${requestScope['javax.servlet.forward.request_uri']}">
            <div class="row">
                <div class="col">
                    <mytags:inputText name="email" labelKey="user.label.email"
                                      type="email"
                                      value="${bean.email}" required="true" />
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <mytags:inputText name="password" labelKey="user.label.password"
                                      type="password"
                                      value="" required="true" />
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <mytags:buttonSubmit labelKey="button.login" />
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <c:url var="registerUrl" value="/register"/>
                    <a href="${registerUrl}">
                        <fmt:message key="user.register.linkText"/>
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
