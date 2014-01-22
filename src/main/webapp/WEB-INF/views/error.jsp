<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>

<html>
    <head>
    
    	<c:if test="${redirectUrl != null}">
    		<script>
                top.location.href = "${redirectUrl}";   
            </script>
    	</c:if>
        
    </head>
    <body>      
        Welcome, ${user.name} ${redirectUrl}!
    </body>
</html>