<?xml version="1.0" encoding="UTF-8" ?>

<!-- New document created with EditiX at Wed Mar 08 06:06:38 CET 2017 -->

<xsl:stylesheet version="2.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns="http://labd/art"
	xpath-default-namespace="http://labd/art">

	<xsl:output method="xml" indent="yes"/>
	
	<xsl:template match="/">
	<art  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
		
<artistes>
		<xsl:apply-templates select="//artistes"/>
		</artistes>
	</art>
	</xsl:template>
	
	<xsl:template match="artistes">
			<artistes>
			<xsl:for-each select="./artiste">
			<artiste>
				<nom><xsl:value-of select="./nom"/></nom>
				<prenom><xsl:value-of select="./prénom"/></prenom>
				<xsl:variable name="name" select="./nom"/>
				<xsl:variable name="prenom" select="./prénom"/>
				<naissance><xsl:value-of select="./naissance"/></naissance>
				<décés><xsl:value-of select="./décès"/></décés>
				
				<xsl:copy-of select="./période"  copy-namespaces="no"/>
				
				<xsl:for-each select="document('cataloque-1.xml')//oeuvre">
				<xsl:sort select="./@genre" order="ascending"/>
					<xsl:variable name="nomO" select =".//nom"/>
					<xsl:if test="$nomO = $name ">
						<oeuvre>
							<titre><xsl:value-of select="./titre"/></titre>
							<date><xsl:value-of select="./date"/></date>
						</oeuvre>
					</xsl:if>	
				</xsl:for-each>
				
			</artiste>
			</xsl:for-each>
			</artistes>
			
			<xsl:copy-of select="//mouvement" copy-namespaces="no"/>
			
		
	</xsl:template>

</xsl:stylesheet>
