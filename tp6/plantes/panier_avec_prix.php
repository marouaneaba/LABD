<?php  header("Content-type: text/xml");?>
<?xml-stylesheet type="text/xsl" href="panier_avec_prix.xsl"?>
<?php
  $local_panier="panier_local.xml";

  echo "<page>
          <params>
            <param name='catalog_xml'>plant_catalog.xml</param>
            <param name='panier_xml'>".$local_panier."</param>
          </params>
        </page>";
  ?>
