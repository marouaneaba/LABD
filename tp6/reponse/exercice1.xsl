<?xml version='1.0' encoding='UTF-8'?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    >
<xsl:output method="html" indent="yes" encoding="UTF-8"
omit-xml-declaration="no"/>
  
  <xsl:template match="/">    
    <html xmlns="http://www.w3.org/1999/xhtml"><head></head>
      <body>
        
        <p>
          <table border="2px" >
			
	<tr>
    		<xsl:for-each select="//PLANT[1]/*">  
  		<th><xsl:value-of select="./name()"/></th>
  	    	</xsl:for-each>
  	</tr>
  	
  	<xsl:for-each select="//PLANT">
  	<xsl:sort select="./COMMON" order="ascending"/>
  		<tr><xsl:for-each select="./*">
  			<td><xsl:value-of select="./text()"></xsl:value-of></td>
  		</xsl:for-each></tr>	
  	</xsl:for-each>
		  </table>
		</p>
    </body>
   </html>
  </xsl:template>
  
</xsl:stylesheet>
