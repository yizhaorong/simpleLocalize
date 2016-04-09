/*
Localizable.strings

GENERATED - DO NOT MODIFY - use the autoLocalizable gem instead.

Created by autoLocalizable.
*/

<#list list as localizable>
<#if (localizable.comment > 0) >

// ${localizable.value}
<#else>
"${localizable.key}" = "${localizable.value}";
</#if>
</#list>