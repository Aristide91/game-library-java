package util;

import model.User;

/**
 * Interface pour les objets pouvant être exportés sous différents formats.
 * Comportement transversal valorisé par la prof.
 */
public interface Exportable {
    /**
     * Exporte les données de l'utilisateur.
     * @param user L'utilisateur dont on veut exporter les données.
     */
    void export(User user);
}
