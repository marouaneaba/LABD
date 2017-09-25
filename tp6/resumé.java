


/*


TP 7:

	/CATALOG/PLANT[1]/COMMON/text()
	/CATALOG/PLANT[1]/*[1]/text()
					  *[1]/name()  -> COMMON/text	
					  */name() -> COMMON/
/*
	-CATALOG
		-PLANT
			-TULIP
	-BOTANICAL
	.
	.
	.
	.
	
	<xsl:for-each
		select ="/PLANT[1]/*/name()">
		<td><xsl:value-of select="."/></td>/*	
	</xsl:for-each>
	
	
*/