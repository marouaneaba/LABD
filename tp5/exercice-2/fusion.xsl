<?xml version="1.0" encoding="UTF-8" ?>

<!-- New document created with EditiX at Thu Feb 18 14:12:09 CET 2015 -->

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://labd/art"
	xpath-default-namespace="http://labd/art">

	<xsl:output method="xml" indent="yes"/>
	
	
<xsl:template match="/">
	<art>
		<artistes>
			<xsl:for-each select=".//artistes/artiste">  
				<nom><xsl:value-of select="./nom"/></nom>
				<prenom><xsl:value-of select="./prenom"/></prenom>
				<naissance><xsl:value-of select="./naissance"/></naissance>
				<deces><xsl:value-of select="./deces"/></deces>
				<periodes>
					<xsl:for-each select="./periode">
						<periode>						
							<mouv><xsl:value-of select="./mouv"/></mouv>
							<debut><xsl:value-of select="./debut"/></debut>
							<fin><xsl:value-of select="./fin"/></fin>
						</periode>
					</xsl:for-each> 
				</periodes>
				<oeuvres>
					<xsl:for-each select="./oeuvre">
						<oeuvre>
							<genre><xsl:value-of select="./genre"/></genre>
							<titre><xsl:value-of select="./titre"/></titre>
							<date><xsl:value-of select="./date"/></date>
						</oeuvre>
					</xsl:for-each> 
				</oeuvres>
			</xsl:for-each>
		</artistes>
		<mouvements>
			<xsl:for-each select="./mouvement">
				<mouvement>
					<nom><xsl:value-of select="./nom"/></nom>
					<description><xsl:value-of select="./description"/></description>
					<periode><xsl:value-of select="./periode"/></periode>
				</mouvement>
			</xsl:for-each> 
		</mouvements>
	</art>
</xsl:template>

</xsl:stylesheet>
