#usr/bin/perl
use strict;
use warnings;
use Lingua::Stem; #Perl modeule for stemming operations
require "get_words.pl";


#Takes reference to an array of documents and returns an inverted index
#Inverted index represented by a hash table (term -> list of document ids)
sub get_idx
{
	
	my $id2doc_ref = shift;
	my $stop_word_ref = shift;
	my %id2doc = %{$id2doc_ref};
	
	#Start creating the INVERTED INDEX
	my %idx = (); #term -> posting list
	foreach my $id (keys %id2doc)
	{
		my @doc = $id2doc{$id};
		@doc = get_words(@doc,$stop_word_ref);
		foreach(@doc)
		{push @{$idx{$_}}, $id;}
	}
	#Yippie!!! Inverted index created
	
	return \%idx;	
}	

1;