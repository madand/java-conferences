<%@ include file="/WEB-INF/jspf/directive/page.jspf" %>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf" %>

<c:set var="pageTitle" scope="request">
    <fmt:message key="user.register.title"/>
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
                    <mytags:inputText name="realName" labelKey="user.label.realName"
                                      type="text"
                                      value="${bean.realName}" required="true" />
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <mytags:inputText name="password" labelKey="user.label.passwordNew"
                                      type="password"
                                      value=""  required="true" />
                </div>
            </div>
            <div class="row">
                <div class="col">
                    <mytags:inputText name="passwordRepeat" labelKey="user.label.passwordNewRepeat"
                                      type="password"
                                      value=""  required="true" />
                </div>
            </div>

            <div class="row">
                <div class="col">
                    <mytags:buttonSubmit labelKey="button.save" />
                </div>
            </div>
        </form>
    </div>
</div>

<%@ include file="/WEB-INF/jspf/footer.jspf" %>
