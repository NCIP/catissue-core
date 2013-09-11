#!/usr/bin/perl
#use warnings;
use DBI;
use IO::Handle;

use POSIX qw( strftime );
my $mmddyyyy = strftime("%d%m%Y%H%M", localtime);

################################# Catching the command line arguments into variables ##########################################################


$Ticket_id=$ARGV[0];
$error="Error";
$errorFileName = "./$error"."_"."$Ticket_id".".log";
open ERROR, '>', $errorFileName or warn $!;
STDERR->fdopen( \*ERROR, 'w' ) or warn $!;


$Ticket_id=$ARGV[0];
$Databasetype=$ARGV[1];
#$Databasetype=ucfirst($Databasetype);
$Host=$ARGV[2];
$Port=$ARGV[3];
$Datbasename=$ARGV[4];
$Username=$ARGV[5];
$Password=$ARGV[6];

$report_title="MonthlyAuditReport";
$report_name=$report_title."_".$mmddyyyy;
$report_title_inside_report="Monthly Audit Report";
$filename="./$report_name.csv";
open (F1,">$filename"); ### Creating a output file

######################################## Database connection ######################################################################################

$dbh = DBI->connect("dbi:$Databasetype:host=$Host;sid=$Datbasename;port=$Port",$Username,$Password)
or warn "Connection Error: $DBI::errstr\n";

$sth = $dbh->prepare("use $Datbasename");
$sth->execute or warn "SQL Error: $DBI::errstr\n";

################################## SQL query to fetch start date, end date and user id from reports_job_details table #########################

$get_db_values = "select report.start_date,report.end_date, report.user_id,user.first_name,user.Last_name from report_job_details report inner join catissue_user user on user.identifier = report.user_id where report.identifier = '$Ticket_id'";

$sth = $dbh->prepare($get_db_values);
$sth->execute or warn "SQL Error: $DBI::errstr\n";
while (@row = $sth->fetchrow_array) {
	print  $row[0].",".$row[1].",".$row[2]."\n";
	$Start_date=$row[0];
	$End_date=$row[1];
	$User_id=$row[2];
	$first_name=$row[3];
	$Last_name=$row[4];
}

print  "$Start_date\n";
print  "$End_date\n";
print  "$Last_name $first_name\n";

#--------------------------------------------- SQL query as per user requirement -----------------------------------------------------------------#
$sth = $dbh->prepare("use $Datbasename");
$sth->execute or warn "SQL Error: $DBI::errstr\n";

$monthly_audit_query_sql = "select cu.identifier,cu.first_name,cu.last_name,cu.login_name, cae.event_type, count(cdal.identifier),cdal.object_name from catissue_user cu,catissue_data_audit_event_log cdal, catissue_audit_event_log cal,catissue_audit_event cae where cdal.identifier = cal.identifier and cal.audit_event_id = cae.identifier and cu.identifier = cae.user_id and cae.event_timestamp BETWEEN '$Start_date' AND '$End_date' group by cdal.object_name,cae.event_type, cu.first_name,cu.last_name,cu.login_name order by cu.identifier";


#-------------------------------- Writing the output into output file -----------------------------------------------------------------------------#
$report="Report Name";
$duration=Duration;
$exported_by="Exported By";

print F1 $report.",".$report_title_inside_report."\n";
print F1 $exported_by.",".$Last_name.'   '.$first_name."\n";
print F1 $duration.",".$Start_date." to ".$End_date."\n\n";

print F1 "Identifier,First Name,Last Name,Login Name,Event Type,Count,Object Name\n";

$sth = $dbh->prepare($monthly_audit_query_sql);
$sth->execute or warn "SQL Error: $DBI::errstr\n";
while (@row1 = $sth->fetchrow_array) {	
	foreach $x(@row1){
		print F1 $x.",";
	}
		print F1 "\n";
}

close ERROR;
close F1;

######################################################################insert_database.pl#################################################################################

#------------------------------------------------- Opening the error file -------------------------------------------------------------------------#
#$error="Error";
open(F,"< ","./$error"."_"."$Ticket_id".".log") || warn $!;

@arr=<F>;
close F;

$error_message=join("",@arr);
$error_message=~s/^\n//gs;
$error_message=~s/\n$//gs;
$error_message =~ s/[^a-zA-Z0-9]+/ /g;

#----------------------- Inserting output file name, execution status, execution end and error if error exists -----------------------------------#

$dbh = DBI->connect("dbi:$Databasetype:host=$Host;sid=$Datbasename;port=$Port",$Username,$Password) or warn "Connection Error: $DBI::errstr\n";
if($error_message eq ""){
	if (-e $filename) { 
		print "File Exists!";
		$sth = $dbh->prepare("use $Datbasename");
		$sth->execute or warn "SQL Error: $DBI::errstr\n";
		my $sth = $dbh->prepare("update report_job_details set file_name = '$filename' , exec_status = 'Completed', exec_end = CURRENT_TIMESTAMP where identifier = '$Ticket_id'");
		$sth->execute() or warn $DBI::errstr;  
	}else{
		print "Program executed successfully without any error but file \" $filename \" does not exist!\n";
	}
}else{
	print "Error occured while running the program.\nPrining error: ".$error_message."\n\n";
	$sth = $dbh->prepare("use $Datbasename");
	$sth->execute or warn "SQL Error: $DBI::errstr\n";
	my $sth = $dbh->prepare("update report_job_details set error_desc = '$error_message', exec_status = 'Error' where identifier = '$Ticket_id'");
	$sth->execute() or warn $DBI::errstr;		
	print "Error inserted in database!\n";
}

