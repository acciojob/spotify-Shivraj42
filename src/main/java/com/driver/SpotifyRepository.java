package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;
    //public HashMap<Song, Artist> songArtistHashMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user= new User(name, mobile);
        users.add(user);
        userPlaylistMap.put(user, new ArrayList<Playlist>());
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist= new Artist(name);
        artists.add(artist);
        artistAlbumMap.put(artist, new ArrayList<Album>());
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
         Artist artist=null;
         for(Artist a:artists){
             if(a.getName().equals(artistName)){
                 artist = a;
                 break;
             }
         }
         if(artist==null) artist=new Artist(artistName);
         Album album= new Album(title);
         albums.add(album);
         albumSongMap.put(album, new ArrayList<>());
         artistAlbumMap.get(artist).add(album);
         return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        Album album= null;
        for(Album a: albums){
            if(a.getTitle().equals(albumName)) {
                album=a;
                break;
            }
        }
        if(album==null) throw new Exception("Album does not exist");
        Song song=new Song(title, length);
        songs.add(song);
        albumSongMap.get(album).add(song);
        songLikeMap.put(song, new ArrayList<>());
        return song;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        User user= null;
        for(User u: users){
            if(u.getMobile().equals(mobile)) {
                user=u;
                break;
            }
        }
        if(user==null) throw new Exception("User does not exist");
        Playlist playlist= new Playlist(title);
        playlists.add(playlist);                              // add to playlist
        creatorPlaylistMap.put(user, playlist);
        playlistListenerMap.put(playlist, new ArrayList<>());
        playlistListenerMap.get(playlist).add(user);
        userPlaylistMap.put(user, new ArrayList<>());
        userPlaylistMap.get(user).add(playlist);
        List<Song> songList= new ArrayList<>();
        for(Song song: songs){
            if(song.getLength()==length) songList.add(song);
        }
        playlistSongMap.put(playlist, songList);

        return playlist;

    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        User user= null;
        for(User u: users){
            if(u.getMobile().equals(mobile)) {
                user=u;
                break;
            }
        }
        if(user==null) throw new Exception("User does not exist");
        Playlist playlist= new Playlist(title);
        playlists.add(playlist);                              // add to playlist
        creatorPlaylistMap.put(user, playlist);
        playlistListenerMap.put(playlist, new ArrayList<>());
        playlistListenerMap.get(playlist).add(user);
        userPlaylistMap.put(user, new ArrayList<>());
        userPlaylistMap.get(user).add(playlist);
        List<Song> songList= new ArrayList<>();
        for(String name: songTitles){
            for(Song song: songs){
                if(song.getTitle().equals(name)) songList.add(song);
            }
        }
        playlistSongMap.put(playlist, songList);
        return playlist;

    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        User user= null;
        Playlist playlist=null;
        for(User u: users){
            if(u.getMobile().equals(mobile)) {
                user=u;
                break;
            }
        }
        if(user==null) throw new Exception("User does not exist");
        for(Playlist p:playlists){
            if(p.getTitle().equals(playlistTitle)) {
                playlist=p;
                break;
            }
        }
        if(playlist==null) throw new Exception("Playlist does not exist");
        if(creatorPlaylistMap.get(user).equals(playlist)) return playlist;
        playlistListenerMap.get(playlist).add(user);
        userPlaylistMap.get(user).add(playlist);
        return playlist;

    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        User user= null;
        Song song=null;
        for(User u: users){
            if(u.getMobile().equals(mobile)) {
                user=u;
                break;
            }
        }
        if(user==null) throw new Exception("User does not exist");
        for(Song s: songs){
            if(s.getTitle().equals(songTitle)){
                song= s;
                break;
            }
        }
        if(song==null) throw new Exception("Song does not exist");
        if(songLikeMap.get(song).contains(user)) return song;
        song.setLikes(song.getLikes()+1);
        songLikeMap.get(song).add(user);
        for(Artist artist:artistAlbumMap.keySet()){
            for(Album album: artistAlbumMap.get(artist)){
                if(albumSongMap.get(album).contains(song)){
                    artist.setLikes(artist.getLikes()+1);
                    return song;
                }
            }
        }
        return song;
    }

    public String mostPopularArtist() {
        int maxLikes=0;
        Artist MPA=null;
        for (Artist artist: artists){
            if(artist.getLikes()>maxLikes){
                maxLikes=artist.getLikes();
                MPA=artist;
            }
        }
        return MPA.getName();
    }

    public String mostPopularSong() {
        int maxLikes=0;
        Song MPS=null;
        for (Song song: songs){
            if(song.getLikes()>maxLikes){
                maxLikes=song.getLikes();
                MPS=song;
            }
        }
        return MPS.getTitle();
    }
}
