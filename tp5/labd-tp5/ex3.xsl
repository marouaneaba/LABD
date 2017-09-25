<?xml version="1.0" encoding="UTF-8"?>

<!-- New document created with EditiX at Thu Mar 02 11:32:33 CET 2017 -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html"/>
	
	<xsl:template match="/">
	<html>
			<body>
				<h3>Les clubs de Ligue 1 <br/> <br/> saison 
        				<xsl:value-of select="(/championnat/@annee)-1"/>-<xsl:value-of select="/championnat/@annee"/> </h3>
				<table border="1" >
					<tr>
						<td align="center"><b> ville</b></td>
						<td align="center"><b>club</b></td>
					</tr>
					<xsl:apply-templates select="//club"/>
				</table>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="club">
		<tr>
					
			<td>
				<xsl:value-of select="ville"/> 
			</td>
			<td>
				<xsl:value-of select="nom"/>
			</td>
		</tr>
	</xsl:template>

</xsl:stylesheet>


