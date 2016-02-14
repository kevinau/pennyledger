<!doctype html>

<html>
  <head>
    <title>About PennyLedger</title>
    <style>
body {
    font-family: Arial, Helvetica, sans-serif;
    font-size: 0.9em;
}
table {
    border: 1px solid #999999;
    border-collapse:collapse;
}
table td,
table th {
    border: 1px solid #999999;
    padding: 0.25em 0.5em;
}
table th {
	text-align: left;
}
    </style>
  </head>
  <body>
    <h1>About PennyLedger</h1>
    <p>PennyLedger was written by Kevin Holloway (kholloway@geckosoftware.com.au)</p>
<#--
    <h2>Config</h2>
    <table>
<#list config as name>
      <tr>
        <td>${name}</td>
        <td>${config.get(name)}</td>
      </tr>
</#list>
    </table>


    <h2>Attributes</h2>
#foreach ($name in $request.getAttributeNames())
##      ${name} = $request.getAttribute($name)<br>
#end
-->
    
    <h2>Parameters</h2>
    ${requestParams?size}
    ${requestParams?entrySet?size}
    <#list requestParams?entrySet>
      <table>
        <#items as entry>
        <tr>
          <td>${entry.key}</td>
          <td>${entry.value}</td>
        </tr>
        </#items>
      </table>
    <#else>
      <i>No parameters</i>
    </#list>
  </body>
</html>
