<?xml version='1.0' encoding='UTF-8'?>
<xsl:stylesheet version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://www.w3.org/1999/xhtml"
    >
<xsl:output method="html" indent="yes" encoding="UTF-8"
omit-xml-declaration="no"/>
  
  <xsl:template match="/">    
    <html xmlns="http://www.w3.org/1999/xhtml"><head></head>
      <body>
        <h3>Les clubs de Ligue 1 <br/> saison 
        	<xsl:value-of select="(/championnat/@annee)-1"/>-<xsl:value-of select="/championnat/@annee"/> :</h3>
        <p>
          <table border="1px">
			<tr>
			  <td>Ville</td>
   	 		  <td>Nom</td>
		    </tr>
			<xsl:for-each select="//club">
			  <tr>
				<td><xsl:value-of select="ville"/></td>
				<td><xsl:value-of select="nom"/></td>
			  </tr>
			</xsl:for-each>
		  </table>
		</p>
    </body>
   </html>
  </xsl:template>
  
</xsl:stylesheet>
