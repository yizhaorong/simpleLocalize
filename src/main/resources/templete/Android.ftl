<?xml version="1.0" encoding="utf-8"?>
<resources>
<#list list as localizable>
    <#if (localizable.comment > 0) >
    <!--${localizable.value} -->
    <#else>
    <string name="${localizable.key}">${localizable.value}</string>
    </#if>
</#list>
</resources>