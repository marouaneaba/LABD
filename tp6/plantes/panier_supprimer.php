<?php
  $local_panier="panier_local.xml";

  $no_item=$_REQUEST['no_item'];
  $cmd="/usr/bin/xsltproc --stringparam no_item \"$no_item\" panier_supprimer.xsl $local_panier > /tmp/panier.xml";
  $cmd.="; mv /tmp/panier.xml ".$local_panier;
  error_log($cmd);
  passthru($cmd);
  echo "<meta http-equiv='refresh' content='0; panier.php' />";

  ?>
