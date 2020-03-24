import React, { useEffect, useContext } from 'react';
import queryString from 'query-string';
import { useHistory } from 'react-router-dom';
import { injectIntl } from 'react-intl';
import Progress from 'AppComponents/Shared/Progress';
import Alert from 'AppComponents/Shared/Alert';
import PropTypes from 'prop-types';
import { ApiContext } from 'AppComponents/Apis/Details/ApiContext';
import Monetization from '../data/Monetization';

const CheckoutSuccessPage = (props) => {
    const { intl } = props;
    const { updateSubscriptionData } = useContext(ApiContext);
    const history = useHistory();
    const { sessionId, workflowId } = queryString.parse(props.location.search);
    useEffect(() => {
        const monetization = new Monetization();
        if (sessionId && workflowId) {
            monetization.completeCheckout(sessionId, workflowId)
                .then((response) => {
                    console.log('Checkout Succesful');
                    history.push('credentials?checkout=successful');
                    if (updateSubscriptionData) {
                        updateSubscriptionData();
                    }
                    Alert.info(intl.formatMessage({
                        defaultMessage: 'Subscribed successfully',
                        id: 'monetization.CustomComponent.CheckoutSucessPage.subscribed.successfully',
                    }));
                })
                .catch((error) => {
                    console.log('Error while completing checkout.');
                    console.log(error);
                    Alert.error(intl.formatMessage({
                        defaultMessage: 'Failed to complete checkout process. Please retry',
                        id: 'monetization.CustomComponent.CheckoutSucessPage.checkout.failed',
                    }));
                    history.push('credentials?checkout=failed');
                });
        } else {
            console.log('Error while completing checkout. Session Id or workflow reference is empty');
            Alert.error(intl.formatMessage({
                defaultMessage: 'Failed to complete checkout process',
                id: 'monetization.CustomComponent.CheckoutSucessPage.invalid.ids',
            }));
            history.push('credentials?checkout=failed');
        }
    }, []);
    return (
        <Progress />
    );
};
CheckoutSuccessPage.propTypes = {
    history: PropTypes.shape({
        push: PropTypes.func.isRequired,
    }).isRequired,
    location: PropTypes.shape({
        search: PropTypes.string,
    }).isRequired,
    intl: PropTypes.shape({
        formatMessage: PropTypes.func.isRequired,
    }).isRequired,
};
export default injectIntl(CheckoutSuccessPage);
