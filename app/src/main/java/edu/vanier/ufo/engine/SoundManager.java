package edu.vanier.ufo.engine;

import edu.vanier.ufo.helpers.ResourcesManager;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Responsible for loading sound media to be played using an id or key. Contains
 * all sounds for use later.
 * <p/>
 * User: cdea
 */
public class SoundManager {
    private final Map<ResourcesManager.SoundDescriptor, AudioClip> soundEffects;

    public SoundManager() {
        this.soundEffects = new HashMap<>();
    }
    
    /**
     * Load a sound into a map to later be played based on the id.
     *
     * @param descriptor
     */
    public void loadSoundEffects(final ResourcesManager.SoundDescriptor descriptor) {
        AudioClip sound = new AudioClip(
            getClass().getResource(descriptor.getPath()).toExternalForm()
        );

        sound.setVolume(descriptor.getVolume());

        if (descriptor.isLooping()) {
            sound.setCycleCount(AudioClip.INDEFINITE);
            sound.setPriority(1);
        }

        soundEffects.put(descriptor, sound);
    }

    /**
     * Lookup a name resource to play sound based on the id.
     *
     * @param descriptor
     */
    public void playSound(final ResourcesManager.SoundDescriptor descriptor) {
        if (descriptor == null)
            return;
        
        if (!soundEffects.containsKey(descriptor))
            this.loadSoundEffects(descriptor);
        
        if (descriptor.isLooping())
            soundEffects
                .values()
                .stream()
                .filter((x) -> x.getCycleCount() == AudioClip.INDEFINITE)
                .forEach(AudioClip::stop);

        soundEffects.get(descriptor).play();
    }

    /**
     * Stop all threads and media players.
     */
    public void shutdown() {
        soundEffects.values().forEach(AudioClip::stop);
    }
}
